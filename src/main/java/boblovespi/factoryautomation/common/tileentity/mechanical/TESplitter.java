package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.Splitter;
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

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TESplitter extends BlockEntity implements ITickable
{
	private float maxSpeed;
	private float maxTorque;
	private MechanicalUser in;
	private MechanicalUser out;
	private int counter = -1;
	private float rotation;

	public TESplitter(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teSplitter, pos, state);
		in = new MechanicalUser(EnumSet.of(state.getValue(HORIZONTAL_FACING)));
		out = new MechanicalUser(EnumSet.of(state.getValue(HORIZONTAL_FACING)
													.getClockWise(), state.getValue(HORIZONTAL_FACING)
													.getCounterClockWise()));
		if (state.getBlock() instanceof Splitter splitter)
		{
			maxSpeed = splitter.maxSpeed;
			maxTorque = splitter.maxTorque;
		}
	}

	@Override
	public void load(CompoundTag compound)
	{
		in.ReadFromNBT(compound.getCompound("in"));
		out.ReadFromNBT(compound.getCompound("out"));

		super.load(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.put("in", in.WriteToNBT());
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
			rotation = (rotation + EnergyConstants.RadiansSecondToDegreesTick(in.GetSpeed())) % 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			Direction facing = getBlockState().getValue(HORIZONTAL_FACING);

			BlockEntity front = Objects.requireNonNull(level).getBlockEntity(worldPosition.relative(facing));

			in.SetSpeedOnFace(facing, (IsMechanicalFace(front, facing.getOpposite()) ? GetUser(front, facing.getOpposite()).GetSpeedOnFace(facing.getOpposite()) : 0));
			in.SetTorqueOnFace(facing, (IsMechanicalFace(front, facing.getOpposite()) ? GetUser(front, facing.getOpposite()).GetTorqueOnFace(facing.getOpposite()) : 0));

			out.SetSpeedOnFace(facing.getClockWise(), in.GetSpeed());
			out.SetTorqueOnFace(facing.getClockWise(), in.GetTorque() / 2f);

			if (in.GetSpeed() > maxSpeed || in.GetTorque() > maxTorque)
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
		return EnergyConstants.RadiansSecondToDegreesTick(in.GetSpeed());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
		{
			if (facing == getBlockState().getValue(HORIZONTAL_FACING) || facing == null)
				return LazyOptional.of(() -> in).cast();
			else if (facing == getBlockState().getValue(HORIZONTAL_FACING).getClockWise() || facing == getBlockState().getValue(HORIZONTAL_FACING).getCounterClockWise())
				return LazyOptional.of(() -> out).cast();
		}
		return super.getCapability(capability, facing);
	}
}
