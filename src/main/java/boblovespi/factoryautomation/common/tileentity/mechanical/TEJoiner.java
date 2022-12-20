package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Joiner;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetSpeedOnFace;
import static boblovespi.factoryautomation.common.util.TEHelper.GetTorqueOnFace;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TEJoiner extends BlockEntity implements ITickable
{
	private float maxSpeed;
	private float maxTorque;
	private MechanicalUser inL;
	private MechanicalUser inR;
	private MechanicalUser out;
	private int counter = -1;
	private float rotation;

	public TEJoiner(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teJoiner.get(), pos, state);
		inL = new MechanicalUser(EnumSet.of(state.getValue(HORIZONTAL_FACING).getCounterClockWise()));
		inR = new MechanicalUser(EnumSet.of(state.getValue(HORIZONTAL_FACING).getClockWise()));
		out = new MechanicalUser(EnumSet.of(state.getValue(HORIZONTAL_FACING)));

		if (state.getBlock() instanceof Joiner joiner)
		{
			maxSpeed = joiner.maxSpeed;
			maxTorque = joiner.maxTorque;
		}
	}

	@Override
	public void load(CompoundTag compound)
	{
		inL.ReadFromNBT(compound.getCompound("inLeft"));
		inR.ReadFromNBT(compound.getCompound("inRight"));
		out.ReadFromNBT(compound.getCompound("out"));

		super.load(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.put("inLeft", inL.WriteToNBT());
		compound.put("inRight", inR.WriteToNBT());
		compound.put("out", out.WriteToNBT());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}

	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
		{
			rotation = (rotation + EnergyConstants.RadiansSecondToDegreesTick(out.GetSpeed())) % 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			var facing = getBlockState().getValue(HORIZONTAL_FACING);
			var left = facing.getCounterClockWise();
			var right = facing.getClockWise();

			var leftTe = level.getBlockEntity(worldPosition.relative(left));
			var rightTe = level.getBlockEntity(worldPosition.relative(right));

			inL.SetTorqueOnFace(left, GetTorqueOnFace(leftTe, left.getOpposite()));
			inL.SetSpeedOnFace(left, GetSpeedOnFace(leftTe, left.getOpposite()));
			inR.SetTorqueOnFace(right, GetTorqueOnFace(rightTe, right.getOpposite()));
			inR.SetSpeedOnFace(right, GetSpeedOnFace(rightTe, right.getOpposite()));

			var lSpeed = inL.GetSpeed();
			var rSpeed = inR.GetSpeed();
			var pDiff = (lSpeed - rSpeed) / Math.max(lSpeed, rSpeed);

			if (-0.01 < pDiff && pDiff < 0.01)
			{
				out.SetSpeedOnFace(facing, (lSpeed + rSpeed) / 2);
				out.SetTorqueOnFace(facing, inL.GetTorque() + inR.GetTorque());
			}
			else
			{
				out.SetSpeedOnFace(facing, 0);
				out.SetTorqueOnFace(facing, 0);
			}

			if (out.GetSpeed() > maxSpeed || out.GetTorque() > maxTorque)
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
		return EnergyConstants.RadiansSecondToDegreesTick(out.GetSpeed());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
		{
			if (facing == getBlockState().getValue(HORIZONTAL_FACING) || facing == null)
				return LazyOptional.of(() -> out).cast();
			else if (facing == getBlockState().getValue(HORIZONTAL_FACING).getClockWise())
				return LazyOptional.of(() -> inR).cast();
			else if (facing == getBlockState().getValue(HORIZONTAL_FACING).getCounterClockWise())
				return LazyOptional.of(() -> inL).cast();
		}
		return super.getCapability(capability, facing);
	}
}