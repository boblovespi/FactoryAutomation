package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 5/6/2018.
 * gearbox, like rotarycraft
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEGearbox extends BlockEntity implements IMechanicalUser, ITickable
{
	public float rotationIn = 0;
	public float rotationOut = 0;
	public float rotationTop = 0;
	public float speedTop = 0;
	private float speedIn;
	private float torqueIn;
	private float speedOut;
	private float torqueOut;
	private Gearbox.GearType inputGear = null;
	private Gearbox.GearType outputGear = null;
	private int inputDurability = -1;
	private int outputDurability = -1;
	private int counter = -1;

	public TEGearbox(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teGearbox, pos, state);
	}

	public boolean SideIsInput(Direction side)
	{
		return getBlockState().getValue(Gearbox.FACING) == side.getOpposite();
	}

	public boolean SideIsOutput(Direction side)
	{
		return getBlockState().getValue(Gearbox.FACING) == side;
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().getValue(Gearbox.FACING).getAxis();
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? speedOut : SideIsInput(side) ? speedIn : 0;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		ForceUpdate(false);
		return SideIsOutput(side) ? torqueOut : SideIsInput(side) ? torqueIn : 0;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{
		if (SideIsInput(side))
			this.speedIn = speed;
		ForceUpdate(false);
	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
	{
		if (SideIsInput(side))
			this.torqueIn = torque;
		ForceUpdate(false);
	}

	@Override
	public void load(CompoundTag compound)
	{
		speedIn = compound.getFloat("speedIn");
		torqueIn = compound.getFloat("torqueIn");
		speedOut = compound.getFloat("speedOut");
		torqueOut = compound.getFloat("torqueOut");

		inputGear = NBTHelper.GetGear(compound, "gearIn");
		outputGear = NBTHelper.GetGear(compound, "gearOut");

		inputDurability = compound.getInt("inputDurability");
		outputDurability = compound.getInt("outputDurability");

		super.load(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.putFloat("speedIn", speedIn);
		compound.putFloat("torqueIn", torqueIn);
		compound.putFloat("speedOut", speedOut);
		compound.putFloat("torqueOut", torqueOut);

		NBTHelper.SetGear(compound, "gearIn", inputGear);
		NBTHelper.SetGear(compound, "gearOut", outputGear);

		compound.putInt("inputDurability", inputDurability);
		compound.putInt("outputDurability", outputDurability);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			ProcessVisualChanges();
			return;
		}

		++counter;
		counter %= 100;

		if (counter == 0)
		{
			ForceUpdate(true);
		}
	}

	public void ForceUpdate(boolean damageGears)
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			ProcessVisualChanges();
			return;
		}

		BlockState state = getBlockState();
		Direction facing = state.getValue(Gearbox.FACING);

		BlockEntity te = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));

		if (IsMechanicalFace(te, facing))
		{
			IMechanicalUser input = GetUser(te, facing);
			torqueIn = input.GetTorqueOnFace(facing);
			speedIn = input.GetSpeedOnFace(facing);

			if (inputGear == null || outputGear == null)
			{
				speedOut = 0;
				torqueOut = 0;
				return;
			}

			torqueOut = (torqueIn * outputGear.scaleFactor) / inputGear.scaleFactor;
			speedOut = (speedIn * inputGear.scaleFactor) / outputGear.scaleFactor;

			if (damageGears && speedIn > 0 && torqueIn > 0)
			{
				inputDurability--;
				outputDurability--;

				if (inputDurability < 0)
					inputGear = null;
				if (outputDurability < 0)
					outputGear = null;
			}

			setChanged();

			/* IMPORTANT */
			BlockState state2 = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);
		}
	}

	/**
	 * Adds a gear to the gearbox
	 *
	 * @param gear the gear which is attempted to add
	 * @return whether adding the gear was successful or not
	 */
	public boolean AddGear(Gearbox.GearType gear, int durability)
	{
		if (inputGear == null)
		{
			inputGear = gear;
			inputDurability = durability;

			setChanged();
			BlockState state2 = Objects.requireNonNull(level).getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);
			return true;

		} else if (outputGear == null)
		{
			outputGear = gear;
			outputDurability = durability;

			setChanged();
			BlockState state2 = Objects.requireNonNull(level).getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);
			return true;
		}
		return false;
	}

	public void RemoveGear()
	{
		if (outputGear != null)
		{
			if (!Objects.requireNonNull(level).isClientSide)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(outputGear), 1);
				stack.setDamageValue(outputGear.durability - outputDurability);
				level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack));
			}

			outputGear = null;
			outputDurability = -1;

			setChanged();
			BlockState state2 = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);
		} else if (inputGear != null)
		{
			if (!Objects.requireNonNull(level).isClientSide)
			{
				ItemStack stack = new ItemStack(FAItems.gear.GetItem(inputGear), 1);
				stack.setDamageValue(inputGear.durability - inputDurability);
				level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack));
			}

			inputGear = null;
			inputDurability = -1;

			setChanged();
			BlockState state2 = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state2, state2, 3);
		}
	}

	public float GetSpeedIn()
	{
		return EnergyConstants.RadiansSecondToDegreesTick(speedIn);
	}

	public float GetSpeedOut()
	{
		return EnergyConstants.RadiansSecondToDegreesTick(speedOut);
	}

	public Gearbox.GearType GetGearIn()
	{
		return inputGear;
	}

	public Gearbox.GearType GetGearOut()
	{
		return outputGear;
	}

	public void ProcessVisualChanges()
	{
		rotationIn = (rotationIn + EnergyConstants.RadiansSecondToDegreesTick(speedIn)) % 360;
		rotationOut = (rotationOut + EnergyConstants.RadiansSecondToDegreesTick(speedOut)) % 360;

		if (inputGear != null)
		{
			int gInTop = 20 - inputGear.scaleFactor;
			speedTop = EnergyConstants.RadiansSecondToDegreesTick((speedIn * inputGear.scaleFactor) / gInTop);
			rotationTop = (rotationTop + speedTop) % 360;
		}

	}

	public void SwitchGears()
	{
		int tempDur = inputDurability;
		Gearbox.GearType tempGear = inputGear;

		inputDurability = outputDurability;
		inputGear = outputGear;
		outputGear = tempGear;
		outputDurability = tempDur;

		setChanged();
		BlockState state2 = Objects.requireNonNull(level).getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state2, state2, 3);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return LazyOptional.empty();
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}
}
