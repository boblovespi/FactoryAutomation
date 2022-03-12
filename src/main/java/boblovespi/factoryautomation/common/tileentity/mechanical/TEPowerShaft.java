package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;
import static net.minecraft.core.Direction.get;

/**
 * Created by Willi on 1/15/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEPowerShaft extends BlockEntity implements IMechanicalUser, ITickable
{
	public float rotation = 0;
	private float speed;
	private float torque;
	private float maxSpeed = 0;
	private float maxTorque = 0;
	private int counter = -1;

	public TEPowerShaft(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.tePowerShaft, pos, state);
		if (state.getBlock() instanceof PowerShaft ps)
		{
			maxSpeed = ps.maxSpeed;
			maxTorque = ps.maxTorque;
		}
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().getValue(PowerShaft.AXIS);
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		return HasConnectionOnSide(side) ? speed : 0;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		return HasConnectionOnSide(side) ? torque : 0;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
	{
		if (HasConnectionOnSide(side))
			this.torque = torque;
	}

	@Override
	public void load(CompoundTag compound)
	{
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");

		super.load(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.putFloat("speed", speed);
		compound.putFloat("torque", torque);
	}

	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			rotation = (rotation + EnergyConstants.RadiansSecondToDegreesTick(speed)) % 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			Axis axis = getBlockState().getValue(PowerShaft.AXIS);

			Direction negativeFacing = get(AxisDirection.NEGATIVE, axis);
			Direction positiveFacing = get(AxisDirection.POSITIVE, axis);

			BlockEntity front = Objects.requireNonNull(level).getBlockEntity(worldPosition.relative(positiveFacing));
			BlockEntity back = level.getBlockEntity(worldPosition.relative(negativeFacing));

			speed = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).GetSpeedOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).GetSpeedOnFace(positiveFacing) : 0)) / 2f;

			torque = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).GetTorqueOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).GetTorqueOnFace(positiveFacing) : 0)) / 2f;

			if (speed > maxSpeed || torque > maxTorque)
			{
				level.destroyBlock(worldPosition, true);
				return;
			}

			setChanged();

			/* IMPORTANT */
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	public float GetSpeed()
	{
		return EnergyConstants.RadiansSecondToDegreesTick(speed);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return super.getCapability(capability, facing);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}
}
