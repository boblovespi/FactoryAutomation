package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;
import static net.minecraft.util.Direction.*;

/**
 * Created by Willi on 1/15/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEPowerShaft extends TileEntity implements IMechanicalUser, ITickableTileEntity
{
	public float rotation = 0;
	private float speed;
	private float torque;
	private int counter = -1;

	public TEPowerShaft()
	{
		super(TileEntityHandler.tePowerShaft);
	}

	@Override
	public boolean hasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().getValue(PowerShaft.AXIS);
	}

	@Override
	public float getSpeedOnFace(Direction side)
	{
		return hasConnectionOnSide(side) ? speed : 0;
	}

	@Override
	public float getTorqueOnFace(Direction side)
	{
		return hasConnectionOnSide(side) ? torque : 0;
	}

	@Override
	public void setSpeedOnFace(Direction side, float speed)
	{
		if (hasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void setTorqueOnFace(Direction side, float torque)
	{
		if (hasConnectionOnSide(side))
			this.torque = torque;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");

		super.load(state, compound);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		compound.putFloat("speed", speed);
		compound.putFloat("torque", torque);

		return super.save(compound);
	}

	@Override
	public void tick()
	{
		if (Objects.requireNonNull(world).isClientSide)
		{
			rotation = (rotation + speed) % 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			Axis axis = getBlockState().getValue(PowerShaft.AXIS);

			Direction negativeFacing = get(AxisDirection.NEGATIVE, axis);
			Direction positiveFacing = get(AxisDirection.POSITIVE, axis);

			TileEntity front = Objects.requireNonNull(world).getTileEntity(levelPosition.relative(positiveFacing));
			TileEntity back = world.getTileEntity(levelPosition.relative(negativeFacing));

			speed = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).getSpeedOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).getSpeedOnFace(positiveFacing) : 0)) / 2f;

			torque = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).getTorqueOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).getTorqueOnFace(positiveFacing) : 0)) / 2f;

			setChanged();

			/* IMPORTANT */
			world.sendBlockUpdated(levelPosition, getBlockState(), getBlockState(), 3);
		}
	}

	public float GetSpeed()
	{
		return speed;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) this);
		return super.getCapability(capability, facing);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(world).getBlockState(levelPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}
}
