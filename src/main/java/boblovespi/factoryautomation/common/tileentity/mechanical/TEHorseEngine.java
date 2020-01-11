package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

/**
 * Created by Willi on 7/4/2019.
 */
public class TEHorseEngine extends TileEntity implements ITickable
{
	private MechanicalUser user;
	private boolean hasHorse = false;
	private UUID horseId;
	private EntityLiving horse;
	private int moveTimer = 0;
	private int angle = 0;

	public TEHorseEngine()
	{
		user = new MechanicalUser(EnumSet.of(Direction.UP));
	}

	@Override
	public void onLoad()
	{
		if (!world.isRemote && hasHorse)
			horse = (EntityLiving) ((WorldServer) world).getEntityFromUuid(horseId);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote || !hasHorse)
			return;
		moveTimer++;
		if (horse == null)
		{
			if (horseId == null)
			{
				hasHorse = false;
				return;
			}
			horse = (EntityLiving) ((WorldServer) world).getEntityFromUuid(horseId);
		}
		if (horse.getNavigator().noPath())
		{
			user.SetSpeedOnFace(Direction.UP, 45f / moveTimer);
			moveTimer = 0;
			angle--;
			angle = angle % 8;
			horse.getNavigator()
				 .tryMoveToXYZ(pos.getX() + 4 * MathHelper.cos((float) (angle * Math.PI / 4f)), pos.getY() - 2,
						 pos.getZ() + 4 * MathHelper.sin((float) (angle * Math.PI / 4f)), 1);
			//			horse.setPosition(pos.getX() + 4 * MathHelper.cos((float) (angle * Math.PI / 4f)), pos.getY() - 2,
			//					pos.getZ() + 4 * MathHelper.sin((float) (angle * Math.PI / 4f)));
		}
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		user.ReadFromNBT(tag.getCompoundTag("user"));
		hasHorse = tag.getBoolean("hasHorse");
		if (hasHorse)
			horseId = tag.getUniqueId("horseId");
		moveTimer = tag.getInteger("moveTimer");
		angle = tag.getInteger("angle");
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		tag.setTag("user", user.WriteToNBT());
		if (hasHorse)
			tag.setUniqueId("horseId", horseId);
		tag.setBoolean("hasHorse", hasHorse);
		tag.setInteger("moveTimer", moveTimer);
		tag.setInteger("angle", angle);
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

	public boolean AttachHorse(EntityLiving horse)
	{
		if (hasHorse)
			return false;
		hasHorse = true;
		this.horse = horse;
		horseId = horse.getUniqueID();
		user.SetTorqueOnFace(Direction.UP, 10);
		user.SetSpeedOnFace(Direction.UP, 0);
		markDirty();
		BlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
		return true;
	}

	public void RemoveHorse()
	{
		if (!hasHorse)
			return;
		hasHorse = false;
		horse = null;
		horseId = null;
		user.SetTorqueOnFace(Direction.UP, 0);
		user.SetSpeedOnFace(Direction.UP, 0);
		markDirty();
		BlockState state2 = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state2, state2, 3);
	}
}
