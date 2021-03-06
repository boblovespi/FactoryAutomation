package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
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

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;
import static net.minecraft.util.Direction.*;

/**
 * Created by Willi on 1/15/2018.
 */

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
	public boolean HasConnectionOnSide(Direction side)
	{
		return side.getAxis() == getBlockState().get(PowerShaft.AXIS);
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
	public void read(CompoundNBT compound)
	{
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");

		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putFloat("speed", speed);
		compound.putFloat("torque", torque);

		return super.write(compound);
	}

	@Override
	public void tick()
	{
		if (world.isRemote)
		{
			rotation = (rotation + speed) % 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			Axis axis = getBlockState().get(PowerShaft.AXIS);

			Direction negativeFacing = getFacingFromAxis(AxisDirection.NEGATIVE, axis);
			Direction positiveFacing = getFacingFromAxis(AxisDirection.POSITIVE, axis);

			TileEntity front = world.getTileEntity(pos.offset(positiveFacing));
			TileEntity back = world.getTileEntity(pos.offset(negativeFacing));

			speed = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).GetSpeedOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).GetSpeedOnFace(positiveFacing) : 0)) / 2f;

			torque = ((IsMechanicalFace(front, negativeFacing) ?
					GetUser(front, negativeFacing).GetTorqueOnFace(negativeFacing) : 0) + (
					IsMechanicalFace(back, positiveFacing) ?
							GetUser(back, positiveFacing).GetTorqueOnFace(positiveFacing) : 0)) / 2f;

			markDirty();

			/* IMPORTANT */
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
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
		read(pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(pos, 0, nbt);
	}
}
