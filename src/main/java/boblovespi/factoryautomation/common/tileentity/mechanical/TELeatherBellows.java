package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static boblovespi.factoryautomation.common.block.mechanical.LeatherBellows.FACING;
import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 5/11/2019.
 */
public class TELeatherBellows extends TileEntity implements ITickable, IBellowsTE
{
	private float counter = 0;
	private int c2 = 0;
	private MechanicalUser mechanicalUser;
	private float lerp = 0;

	public TELeatherBellows()
	{
		mechanicalUser = new MechanicalUser();
	}

	@Override
	public void onLoad()
	{
		EnumFacing dir = world.getBlockState(pos).getValue(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.getOpposite()));
	}

	public void Blow()
	{
		EnumFacing facing = world.getBlockState(pos).getValue(PaperBellows.FACING);
		TileEntity te = world.getTileEntity(pos.offset(facing));
		if (te == null)
			return;
		IBellowsable capability = te.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		if (capability != null)
			capability.Blow(MathHelper.clamp(mechanicalUser.GetTorque() / 30f, 0.5f, 1), 50);
		world.playSound(null, pos, SoundEvents.ENTITY_ENDERDRAGON_FLAP, SoundCategory.BLOCKS, 0.8f, 1.5f);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
		{
			counter -= mechanicalUser.GetSpeed() / 10f;
			if (counter <= 0)
			{
				counter = 100;
			}
			lerp = Math.abs(2 * (counter / 100f) - 1);
			return;
		}
		counter -= mechanicalUser.GetSpeed() / 10f;
		if (counter <= 0)
		{
			counter = 100;
			Blow();
		}
		c2--;
		if (c2 <= 0)
		{
			EnumFacing facing = world.getBlockState(pos).getValue(FACING);
			TileEntity te = world.getTileEntity(pos.offset(facing.getOpposite()));
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), GetUser(te, facing).GetTorqueOnFace(facing));
			} else
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), 0);
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), 0);
			}
			markDirty();
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			c2 = 4;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		mechanicalUser.ReadFromNBT(tag.getCompoundTag("mechanicalUser"));
		counter = tag.getFloat("counter");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setTag("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.setFloat("counter", counter);
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
	public float GetLerp()
	{
		return lerp;
	}

	@Override
	public float GetLerpSpeed()
	{
		return (counter > 50 ? -1 : 1) * mechanicalUser.GetSpeed() / 400f;
	}
}
