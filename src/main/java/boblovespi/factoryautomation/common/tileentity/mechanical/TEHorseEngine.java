package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Mob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Willi on 7/4/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEHorseEngine extends BlockEntity implements ITickable
{
	private final MechanicalUser user;
	private boolean hasHorse = false;
	private UUID horseId;
	private Mob horse;
	private int moveTimer = 0;
	private int angle = 0;
	private final LazyOptional<MechanicalUser> lazyUser;
	private boolean firstTick = true;

	public TEHorseEngine(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teHorseEngine, pos, state);
		user = new MechanicalUser(EnumSet.of(Direction.UP));
		lazyUser = LazyOptional.of(() -> user);
	}

	public void FirstLoad()
	{
		if (!Objects.requireNonNull(level).isClientSide && hasHorse)
			horse = (Mob) ((ServerLevel) level).getEntity(horseId);
		firstTick = false;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide || !hasHorse)
			return;
		if (firstTick)
			FirstLoad();
		moveTimer++;
		if (horse == null)
		{
			if (horseId == null)
			{
				hasHorse = false;
				return;
			}
			horse = (Mob) ((ServerLevel) level).getEntity(horseId);
		}
		if (Objects.requireNonNull(horse).getNavigation().isDone())
		{
			user.SetSpeedOnFace(Direction.UP, 45f / moveTimer);
			moveTimer = 0;
			angle--;
			angle = angle % 8;
			horse.getNavigation()
				 .moveTo(worldPosition.getX() + 4 * Mth.cos((float) (angle * Math.PI / 4f)), worldPosition.getY() - 2,
						 worldPosition.getZ() + 4 * Mth.sin((float) (angle * Math.PI / 4f)), 1);
			//			horse.setPosition(worldPosition.getX() + 4 * MathHelper.cos((float) (angle * Math.PI / 4f)), levelPosition.getY() - 2,
			//					worldPosition.getZ() + 4 * MathHelper.sin((float) (angle * Math.PI / 4f)));
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		user.ReadFromNBT(tag.getCompound("user"));
		hasHorse = tag.getBoolean("hasHorse");
		if (hasHorse)
			horseId = tag.getUUID("horseId");
		moveTimer = tag.getInt("moveTimer");
		angle = tag.getInt("angle");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.put("user", user.WriteToNBT());
		if (hasHorse)
			tag.putUUID("horseId", horseId);
		tag.putBoolean("hasHorse", hasHorse);
		tag.putFloat("moveTimer", moveTimer);
		tag.putFloat("angle", angle);
	}

	//	@Nullable
	//	@Override
	//	public SPacketUpdateTileEntity getUpdatePacket()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		int meta = getBlockMetadata();
	//		return new SPacketUpdateTileEntity(worldPosition, meta, nbt);
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

	@SuppressWarnings("UnusedReturnValue")
	public boolean AttachHorse(Mob horse)
	{
		if (hasHorse)
			return false;
		hasHorse = true;
		this.horse = horse;
		horseId = horse.getUUID();
		user.SetTorqueOnFace(Direction.UP, 10);
		user.SetSpeedOnFace(Direction.UP, 0);
		setChanged();
		BlockState state2 = Objects.requireNonNull(level).getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state2, state2, 3);
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
		setChanged();
		BlockState state2 = Objects.requireNonNull(level).getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state2, state2, 3);
	}
}
