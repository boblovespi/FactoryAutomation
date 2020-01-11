package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import net.minecraft.block.state.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.mechanical.HandCrank.INVERTED;
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
	public boolean inverted = false;

	public TEHandCrank()
	{
		mechanicalUser = new MechanicalUser(EnumSet.of(Direction.DOWN));
		isRotating = false;
	}

	@Override
	public void onLoad()
	{
		inverted = world.getBlockState(pos).getValue(INVERTED);
		mechanicalUser.SetSides(EnumSet.of(inverted ? Direction.UP : Direction.DOWN));
	}

	public void Rotate()
	{
		if (!isRotating)
		{
			isRotating = true;

			mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 1f);
			mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, 1f);

			markDirty();

			/* IMPORTANT */
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
		}
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		mechanicalUser.ReadFromNBT(tag.getCompoundTag("mechanicalUser"));
		rotation = tag.getFloat("rotation");
		isRotating = tag.getBoolean("isRotating");
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		tag.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.setFloat("rotation", rotation);
		tag.setBoolean("isRotating", isRotating);
		return super.writeToNBT(tag);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		return capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return (T) mechanicalUser;
		return null;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
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
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		readFromNBT(tag);
	}

	@Override
	public CompoundNBT getTileData()
	{
		CompoundNBT nbt = new CompoundNBT();
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

				mechanicalUser.SetTorqueOnFace(inverted ? Direction.UP : Direction.DOWN, 0);
				mechanicalUser.SetSpeedOnFace(inverted ? Direction.UP : Direction.DOWN, 0);

				markDirty();

				/* IMPORTANT */
				BlockState state2 = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state2, state2, FORCE_BLOCK_UPDATE | SEND_TO_CLIENT);
			}
		}
	}

	public boolean IsRotating()
	{
		return isRotating;
	}
}
