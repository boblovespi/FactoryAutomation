package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.util.capability.IMechanicalUser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;
import static net.minecraft.util.EnumFacing.*;

/**
 * Created by Willi on 1/15/2018.
 */

public class TEPowerShaft extends TileEntity implements IMechanicalUser, ITickable
{
	public float rotation = 0;
	private float speed;
	private float torque;
	private int counter = -1;

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return side.getAxis() == world.getBlockState(pos).getValue(PowerShaft.AXIS);
	}

	@Override
	public float GetSpeedOnFace(EnumFacing side)
	{
		return HasConnectionOnSide(side) ? speed : 0;
	}

	@Override
	public float GetTorqueOnFace(EnumFacing side)
	{
		return HasConnectionOnSide(side) ? torque : 0;
	}

	@Override
	public void SetSpeedOnFace(EnumFacing side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(EnumFacing side, float torque)
	{
		if (HasConnectionOnSide(side))
			this.torque = torque;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		speed = compound.getFloat("speed");
		torque = compound.getFloat("torque");

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setFloat("speed", speed);
		compound.setFloat("torque", torque);

		return super.writeToNBT(compound);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
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
			IBlockState state = world.getBlockState(pos);
			Axis axis = state.getValue(PowerShaft.AXIS);

			TileEntity front = world.getTileEntity(pos.offset(getFacingFromAxis(AxisDirection.POSITIVE, axis)));
			TileEntity back = world.getTileEntity(pos.offset(getFacingFromAxis(AxisDirection.NEGATIVE, axis)));

			speed = ((IsMechanicalFace(front, getFacingFromAxis(AxisDirection.NEGATIVE, axis)) ?
					((IMechanicalUser) front).GetSpeedOnFace(getFacingFromAxis(AxisDirection.NEGATIVE, axis)) : 0) + (
					IsMechanicalFace(back, getFacingFromAxis(AxisDirection.POSITIVE, axis)) ?
							((IMechanicalUser) back).GetSpeedOnFace(getFacingFromAxis(AxisDirection.POSITIVE, axis)) :
							0)) / 2f;

			torque = ((IsMechanicalFace(front, getFacingFromAxis(AxisDirection.NEGATIVE, axis)) ?
					((IMechanicalUser) front).GetTorqueOnFace(getFacingFromAxis(AxisDirection.NEGATIVE, axis)) : 0) + (
					IsMechanicalFace(back, getFacingFromAxis(AxisDirection.POSITIVE, axis)) ?
							((IMechanicalUser) back).GetTorqueOnFace(getFacingFromAxis(AxisDirection.POSITIVE, axis)) :
							0)) / 2f;

			markDirty();

			/* IMPORTANT */
			IBlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);

		}
	}

	public float GetSpeed()
	{
		return speed;
	}
}
