package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
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
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.machine.TripHammerController.*;

/**
 * Created by Willi on 8/13/2018.
 */
public class TETripHammerController extends TileEntity implements IMultiblockControllerTE, ITickableTileEntity
{
	public static final String MULTIBLOCK_ID = "trip_hammer";
	private ItemStackHandler itemHandler;
	private MechanicalUser mechanicalUser;
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
				markDirty();
			}
		};
		mechanicalUser = new MechanicalUser();
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().get(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.rotateY(), dir.rotateYCCW()));
		firstTick = false;
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	@Override
	public boolean IsStructureValid()
	{
		return isValid;
	}

	@Override
	public void CreateStructure()
	{
		SetStructureValid();
		MultiblockHelper.CreateStructure(world, pos, MULTIBLOCK_ID, getBlockState().get(FACING));
		world.setBlockState(pos, getBlockState().with(MULTIBLOCK_COMPLETE, BlockstateEnum.TRUE));
	}

	@Override
	public void BreakStructure()
	{
		SetStructureInvalid();
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID, getBlockState().get(FACING));
		world.setBlockState(pos, getBlockState().with(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE));
	}

	public boolean PutItem(ItemStack item)
	{
		if (itemHandler.getStackInSlot(1).isEmpty() && itemHandler.getStackInSlot(0).isEmpty())
		{
			if (itemHandler.insertItem(0, item.split(1), false).isEmpty())
				return true;
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
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		if (offset[0] == 5 && offset[1] == 1 && offset[2] == 0 && side.getAxis() == getBlockState().get(FACING)
																								   .rotateY().getAxis()
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
		if (world.isRemote)
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
						markDirty();
						timeLeftInRecipe = recipe.time;
					}
				} else
				{
					currentRecipe = TripHammerRecipe.STRING_TRIP_HAMMER_RECIPE_MAP.get(currentRecipeString);
				}
			} else
			{
				if (mechanicalUser.GetTorque() >= currentRecipe.torque)
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
				markDirty();
			}
			Direction facing = getBlockState().get(FACING);

			BlockPos pos2 = MultiblockHelper.AddWithRotation(pos, 5, 1, 0, facing);
			Direction rotateY = facing.rotateY();
			Direction rotateYCCW = facing.rotateYCCW();
			TileEntity teCW = world.getTileEntity(pos2.offset(rotateY, 1));
			TileEntity teCCW = world.getTileEntity(pos2.offset(rotateYCCW, 1));

			if (TEHelper.IsMechanicalFace(teCW, rotateYCCW))
			{
				mechanicalUser.SetSpeedOnFace(rotateY, TEHelper.GetUser(teCW, rotateYCCW).GetSpeedOnFace(rotateYCCW));
				mechanicalUser.SetTorqueOnFace(rotateY, TEHelper.GetUser(teCW, rotateYCCW).GetTorqueOnFace(rotateYCCW));
			} else if (TEHelper.IsMechanicalFace(teCCW, rotateY))
			{
				mechanicalUser.SetSpeedOnFace(rotateYCCW, TEHelper.GetUser(teCCW, rotateY).GetSpeedOnFace(rotateY));
				mechanicalUser.SetTorqueOnFace(rotateYCCW, TEHelper.GetUser(teCCW, rotateY).GetTorqueOnFace(rotateY));
			}
		}
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		super.read(nbt);

		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		mechanicalUser.ReadFromNBT(nbt.getCompound("mechanicalUser"));
		currentRecipeString = nbt.getString("recipe");
		timeLeftInRecipe = nbt.getFloat("timeLeftInRecipe");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.put("mechanicalUser", mechanicalUser.WriteToNBT());
		nbt.putString("recipe", currentRecipeString);
		nbt.putFloat("timeLeftInRecipe", timeLeftInRecipe);

		return super.write(nbt);
	}
}
