package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.block.machine.TripHammerController;
import boblovespi.factoryautomation.common.block.machine.TripHammerController.BlockstateEnum;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.machine.TripHammerController.FACING;
import static boblovespi.factoryautomation.common.block.machine.TripHammerController.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 8/13/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TETripHammerController extends BlockEntity implements IMultiblockControllerTE, ITickable, IAnimatable
{
	public static final String MULTIBLOCK_ID = "trip_hammer";
	private final ItemStackHandler itemHandler;
	private final MechanicalUser mechanicalUser;
	private boolean isValid;
	private TripHammerRecipe currentRecipe = null;
	private String currentRecipeString = "none";
	private float timeLeftInRecipe = -1;
	private boolean firstTick = true;
	private AnimationFactory factory;

	public TETripHammerController(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teTripHammerController.get(), pos, state);
		itemHandler = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
			}
		};
		mechanicalUser = new MechanicalUser();
		factory = new AnimationFactory(this);
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().getValue(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.getClockWise(), dir.getCounterClockWise()));
		isValid = getBlockState().getValue(MULTIBLOCK_COMPLETE) == BlockstateEnum.TRUE;
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
		MultiblockHelper.CreateStructure(level, worldPosition, MULTIBLOCK_ID, getBlockState().getValue(FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.TRUE));
	}

	@Override
	public void BreakStructure()
	{
		SetStructureInvalid();
		MultiblockHelper.BreakStructure(level, worldPosition, MULTIBLOCK_ID, getBlockState().getValue(FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, getBlockState().setValue(MULTIBLOCK_COMPLETE, BlockstateEnum.FALSE));
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
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
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

			if (!isValid)
				return;

			Direction facing = getBlockState().getValue(FACING);

			BlockPos pos2 = MultiblockHelper.AddWithRotation(worldPosition, 5, 1, 0, facing);
			Direction clockWise = facing.getClockWise();
			Direction counterClockWise = facing.getCounterClockWise();
			BlockEntity teCW = level.getBlockEntity(pos2.relative(clockWise, 1));
			BlockEntity teCCW = level.getBlockEntity(pos2.relative(counterClockWise, 1));

			if (TEHelper.IsMechanicalFace(teCW, counterClockWise) && TEHelper.IsMechanicalFace(teCCW, clockWise))
			{
				mechanicalUser.SetSpeedOnFace(clockWise, 0);
				mechanicalUser.SetTorqueOnFace(clockWise, 0);
			} else if (TEHelper.IsMechanicalFace(teCW, counterClockWise))
			{
				mechanicalUser.SetSpeedOnFace(clockWise, TEHelper.GetUser(teCW, counterClockWise).GetSpeedOnFace(counterClockWise));
				mechanicalUser.SetTorqueOnFace(clockWise, TEHelper.GetUser(teCW, counterClockWise).GetTorqueOnFace(counterClockWise));
			} else if (TEHelper.IsMechanicalFace(teCCW, clockWise))
			{
				mechanicalUser.SetSpeedOnFace(counterClockWise, TEHelper.GetUser(teCCW, clockWise).GetSpeedOnFace(clockWise));
				mechanicalUser.SetTorqueOnFace(counterClockWise, TEHelper.GetUser(teCCW, clockWise).GetTorqueOnFace(clockWise));
			} else
			{
				mechanicalUser.SetSpeedOnFace(clockWise, 0);
				mechanicalUser.SetTorqueOnFace(clockWise, 0);
			}

			ItemStack stack = itemHandler.getStackInSlot(0);
			if (stack.isEmpty() || !itemHandler.getStackInSlot(1).isEmpty())
				return;
			if (currentRecipe == null)
			{
				if ("none".equals(currentRecipeString))
				{
					TripHammerRecipe recipe = level.getRecipeManager().getAllRecipesFor(TripHammerRecipe.TYPE).stream().filter(r -> r.itemInput.test(stack)).findFirst().orElse(null);
					currentRecipe = recipe;
					if (recipe != null)
					{
						currentRecipeString = recipe.name;
						setChanged();
						timeLeftInRecipe = recipe.time;
					}
				} else
				{
					currentRecipe = level.getRecipeManager().getAllRecipesFor(TripHammerRecipe.TYPE).stream().filter(r -> r.name.equals(currentRecipeString)).findFirst().orElse(null);
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
					level.playSound(null, worldPosition, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.3f, 0.5f);
				}
				setChanged();
			}
		}
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);

		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		mechanicalUser.ReadFromNBT(nbt.getCompound("mechanicalUser"));
		currentRecipeString = nbt.getString("recipe");
		timeLeftInRecipe = nbt.getFloat("timeLeftInRecipe");
		isValid = nbt.getBoolean("isValid");
	}

	@Override
	public void saveAdditional(CompoundTag nbt)
	{
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.put("mechanicalUser", mechanicalUser.WriteToNBT());
		nbt.putString("recipe", currentRecipeString);
		nbt.putFloat("timeLeftInRecipe", timeLeftInRecipe);
		nbt.putBoolean("isValid", isValid);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, event -> {
			if (isValid && mechanicalUser.GetSpeed() >= 1)
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.active", true));
				return PlayState.CONTINUE;
			}
			else return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
}
