package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.machine.TripHammerController.*;

/**
 * Created by Willi on 8/13/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TETripHammerController extends TileEntity implements IMultiblockControllerTE, ITickableTileEntity
{
	public static final String MULTIBLOCK_ID = "trip_hammer";
	private final ItemStackHandler itemHandler;
	private final MechanicalUser mechanicalUser;
	private boolean isValid;
	private TripHammerRecipe currentRecipe = null;
	private String currentRecipeString = "none";
	private float timeLeftInRecipe = -1;
	private boolean firstTick = true;

	public TETripHammerController()
	{
		super(TileEntityHandler.teTripHammerController);
		itemHandler = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
			}
		};
		mechanicalUser = new MechanicalUser();
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().getValue(FACING);
		mechanicalUser.setSides(EnumSet.of(dir.getClockWise(), dir.getCounterClockWise()));
		firstTick = false;
	}

	@Override
	public void setStructureValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	@Override
	public boolean isStructureValid()
	{
		return isValid;
	}

	@Override
	public void createStructure()
	{
		setStructureValid();
		MultiblockHelper.CreateStructure(level, levelPosition, MULTIBLOCK_ID, getBlockState().getValue(FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(levelPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.TRUE));
	}

	@Override
	public void breakStructure()
	{
		setStructureInvalid();
		MultiblockHelper.BreakStructure(level, levelPosition, MULTIBLOCK_ID, getBlockState().getValue(FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(levelPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE));
	}

	public boolean PutItem(ItemStack item)
	{
		if (itemHandler.getStackInSlot(1).isEmpty() && itemHandler.getStackInSlot(0).isEmpty())
		{
			return itemHandler.insertItem(0, item.split(1), false).isEmpty();
		}
		return false;
	}

	public ItemStack TakeItem()
	{
		if (itemHandler.getStackInSlot(0).isEmpty())
			return itemHandler.extractItem(1, 64, false);
		currentRecipe = null;
		currentRecipeString = "none";
		return itemHandler.extractItem(0, 64, false);
	}

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 *
	 * @param capability the type of capability to get
	 * @param offset     the offset of the multiblock part
	 * @param side       the side which is accessed
	 * @return the capability implementation which to use
	 */
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, int[] offset, Direction side)
	{
		if (offset[0] == 5 && offset[1] == 1 && offset[2] == 0 && side.getAxis() == getBlockState().getValue(FACING)
																								   .getClockWise().getAxis()
				&& capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) mechanicalUser);
		return LazyOptional.empty();
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		// Todo: clean up empty if statement.
		if (Objects.requireNonNull(level).isClientSide)
		{
			// do rendering calculations
		} else
		{
			if (firstTick)
				FirstLoad();

			ItemStack stack = itemHandler.getStackInSlot(0);
			if (stack.isEmpty() || !itemHandler.getStackInSlot(1).isEmpty())
				return;
			if (currentRecipe == null)
			{
				if ("none".equals(currentRecipeString))
				{
					TripHammerRecipe recipe = TripHammerRecipe.FindRecipe(stack);
					currentRecipe = recipe;
					if (recipe != null)
					{
						currentRecipeString = recipe.name;
						setChanged();
						timeLeftInRecipe = recipe.time;
					}
				} else
				{
					currentRecipe = TripHammerRecipe.STRING_TRIP_HAMMER_RECIPE_MAP.get(currentRecipeString);
				}
			} else
			{
				if (mechanicalUser.getTorque() >= currentRecipe.torque)
				{
					timeLeftInRecipe -= 1; // TODO: calculate actual time changing scale
				}
				if (timeLeftInRecipe < 0)
				{
					itemHandler.extractItem(0, 1, false);
					itemHandler.insertItem(1, currentRecipe.itemOutput.copy(), false);
					currentRecipe = null;
					currentRecipeString = "none";

				}
				setChanged();
			}
			Direction facing = getBlockState().getValue(FACING);

			BlockPos pos2 = MultiblockHelper.AddWithRotation(levelPosition, 5, 1, 0, facing);
			Direction clockWise = facing.getClockWise();
			Direction counterClockWise = facing.getCounterClockWise();
			TileEntity teCW = level.getBlockEntity(pos2.relative(clockWise, 1));
			TileEntity teCCW = level.getBlockEntity(pos2.relative(counterClockWise, 1));

			if (TEHelper.IsMechanicalFace(teCW, counterClockWise))
			{
				mechanicalUser.setSpeedOnFace(clockWise, TEHelper.GetUser(teCW, counterClockWise).getSpeedOnFace(counterClockWise));
				mechanicalUser.setTorqueOnFace(clockWise, TEHelper.GetUser(teCW, counterClockWise).getTorqueOnFace(counterClockWise));
			} else if (TEHelper.IsMechanicalFace(teCCW, clockWise))
			{
				mechanicalUser.setSpeedOnFace(counterClockWise, TEHelper.GetUser(teCCW, clockWise).getSpeedOnFace(clockWise));
				mechanicalUser.setTorqueOnFace(counterClockWise, TEHelper.GetUser(teCCW, clockWise).getTorqueOnFace(clockWise));
			}
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);

		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		mechanicalUser.loadFromNBT(nbt.getCompound("mechanicalUser"));
		currentRecipeString = nbt.getString("recipe");
		timeLeftInRecipe = nbt.getFloat("timeLeftInRecipe");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt)
	{
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.put("mechanicalUser", mechanicalUser.saveToNBT());
		nbt.putString("recipe", currentRecipeString);
		nbt.putFloat("timeLeftInRecipe", timeLeftInRecipe);

		return super.save(nbt);
	}
}
