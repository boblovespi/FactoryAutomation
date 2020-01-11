package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
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

import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static boblovespi.factoryautomation.common.util.TEHelper.IsMechanicalFace;

/**
 * Created by Willi on 6/21/2019.
 */
public class TEBevelGear extends TileEntity implements ITickable
{
	public float rotation = 0;
	private MechanicalUser user;
	private int counter = -1;

	public TEBevelGear()
	{
		user = new MechanicalUser();
	}

	@Override
	public void onLoad()
	{
		BlockState state = world.getBlockState(pos);
		Direction negativeFacing = BevelGear.GetNegative(state);
		Direction positiveFacing = state.getValue(BevelGear.FACING);
		user.SetSides(EnumSet.of(negativeFacing, positiveFacing));
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		user.ReadFromNBT(tag.getCompoundTag("user"));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		tag.setTag("user", user.WriteToNBT());
		return super.writeToNBT(tag);
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
		if (world.isRemote)
		{
			rotation += user.GetSpeed();
			rotation %= 360;
			return;
		}

		++counter;
		counter %= 4;

		if (counter == 0)
		{
			BlockState state = world.getBlockState(pos);

			Direction negativeFacing = BevelGear.GetNegative(state);
			Direction positiveFacing = state.getValue(BevelGear.FACING);

			TileEntity front = world.getTileEntity(pos.offset(positiveFacing));
			TileEntity back = world.getTileEntity(pos.offset(negativeFacing));

			user.SetSpeedOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).GetSpeedOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).GetSpeedOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			user.SetTorqueOnFace(negativeFacing, ((IsMechanicalFace(front, positiveFacing.getOpposite()) ?
					GetUser(front, positiveFacing.getOpposite()).GetTorqueOnFace(positiveFacing.getOpposite()) : 0) + (
					IsMechanicalFace(back, negativeFacing.getOpposite()) ?
							GetUser(back, negativeFacing.getOpposite()).GetTorqueOnFace(negativeFacing.getOpposite()) :
							0)) / 2f);

			markDirty();

			/* IMPORTANT */
			BlockState state2 = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state2, state2, 3);

		}
	}

	public float GetSpeed()
	{
		return user.GetSpeed();
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
			return (T) user;
		return null;
	}
}
