package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.FORCE_BLOCK_UPDATE;
import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.SEND_TO_CLIENT;

/**
 * Created by Willi on 9/3/2018.
 */
public class TEHandCrank extends TileEntity implements ITickable
{

	private static final float SPEED = 1;
	public float rotation = 0;
	private MechanicalUser mechanicalUser;
	private boolean isRotating;

	public TEHandCrank()
	{
		mechanicalUser = new MechanicalUser(EnumSet.of(EnumFacing.DOWN));
		isRotating = false;
	}

	public void Rotate()
	{
		if (!isRotating)
		{
			isRotating = true;

			mechanicalUser.SetTorqueOnFace(EnumFacing.DOWN, 1f);
			mechanicalUser.SetSpeedOnFace(EnumFacing.DOWN, 1f);

			markDirty();

			/* IMPORTANT */
			IBlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		mechanicalUser.ReadFromNBT(tag.getCompoundTag("mechanicalUser"));
		rotation = tag.getFloat("rotation");
		isRotating = tag.getBoolean("isRotating");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.setFloat("rotation", rotation);
		tag.setBoolean("isRotating", isRotating);
		return super.writeToNBT(tag);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) mechanicalUser;
		return null;
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

	@SuppressWarnings("MethodCallSideOnly")
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
		if (isRotating)
		{
			rotation += SPEED;

			if (rotation >= 360)
			{
				rotation = 0;
				isRotating = false;

				mechanicalUser.SetTorqueOnFace(EnumFacing.DOWN, 0);
				mechanicalUser.SetSpeedOnFace(EnumFacing.DOWN, 0);

				markDirty();

				/* IMPORTANT */
				IBlockState state2 = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state2, state2, FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
			}
		}
	}

	public boolean IsRotating()
	{
		return isRotating;
	}
}
