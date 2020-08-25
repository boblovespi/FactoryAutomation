package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

/**
 * Created by Willi on 7/4/2019.
 */
public class TEHorseEngine extends TileEntity implements ITickableTileEntity
{
	private MechanicalUser user;
	private boolean hasHorse = false;
	private UUID horseId;
	private MobEntity horse;
	private int moveTimer = 0;
	private int angle = 0;
	private LazyOptional<MechanicalUser> lazyUser;

	public TEHorseEngine()
	{
		super(TileEntityHandler.teHorseEngine);
		user = new MechanicalUser(EnumSet.of(Direction.UP));
		lazyUser = LazyOptional.of(() -> user);
	}

	@Override
	public void onLoad()
	{
		if (!world.isRemote && hasHorse)
			horse = (MobEntity) ((ServerWorld) world).getEntityByUuid(horseId);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
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
			horse = (MobEntity) ((ServerWorld) world).getEntityByUuid(horseId);
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
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		user.ReadFromNBT(tag.getCompound("user"));
		hasHorse = tag.getBoolean("hasHorse");
		if (hasHorse)
			horseId = tag.getUniqueId("horseId");
		moveTimer = tag.getInt("moveTimer");
		angle = tag.getInt("angle");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("user", user.WriteToNBT());
		if (hasHorse)
			tag.putUniqueId("horseId", horseId);
		tag.putBoolean("hasHorse", hasHorse);
		tag.putFloat("moveTimer", moveTimer);
		tag.putFloat("angle", angle);
		return super.write(tag);
	}

	//	@Nullable
	//	@Override
	//	public SPacketUpdateTileEntity getUpdatePacket()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		int meta = getBlockMetadata();
	//		return new SPacketUpdateTileEntity(pos, meta, nbt);
	//	}
	//
	//	@SuppressWarnings("MethodCallSideOnly")
	//	@Override
	//	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	//	{
	//		this.readFromNBT(pkt.getNbtCompound());
	//	}
	//
	//	@Override
	//	public CompoundNBT getUpdateTag()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		return nbt;
	//	}
	//
	//	@Override
	//	public void handleUpdateTag(CompoundNBT tag)
	//	{
	//		readFromNBT(tag);
	//	}
	//
	//	@Override
	//	public CompoundNBT getTileData()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		return nbt;
	//	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return lazyUser.cast();
		return LazyOptional.empty();
	}

	public boolean AttachHorse(MobEntity horse)
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
