package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import mcp.MethodsReturnNonnullByDefault;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Willi on 7/4/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEHorseEngine extends TileEntity implements ITickableTileEntity
{
	private final MechanicalUser user;
	private boolean hasHorse = false;
	private UUID horseId;
	private MobEntity horse;
	private int moveTimer = 0;
	private int angle = 0;
	private final LazyOptional<MechanicalUser> lazyUser;
	private boolean firstTick = true;

	public TEHorseEngine()
	{
		super(TileEntityHandler.teHorseEngine);
		user = new MechanicalUser(EnumSet.of(Direction.UP));
		lazyUser = LazyOptional.of(() -> user);
	}

	public void FirstLoad()
	{
		if (!Objects.requireNonNull(world).isClientSide && hasHorse)
			horse = (MobEntity) ((ServerWorld) world).getEntity(horseId);
		firstTick = false;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(world).isClientSide || !hasHorse)
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
			horse = (MobEntity) ((ServerWorld) world).getEntity(horseId);
		}
		if (Objects.requireNonNull(horse).getNavigation().isDone())
		{
			user.setSpeedOnFace(Direction.UP, 45f / moveTimer);
			moveTimer = 0;
			angle--;
			angle = angle % 8;
			horse.getNavigation()
				 .moveTo(levelPosition.getX() + 4 * MathHelper.cos((float) (angle * Math.PI / 4f)), levelPosition.getY() - 2,
						 levelPosition.getZ() + 4 * MathHelper.sin((float) (angle * Math.PI / 4f)), 1);
			//			horse.setPosition(levelPosition.getX() + 4 * MathHelper.cos((float) (angle * Math.PI / 4f)), levelPosition.getY() - 2,
			//					levelPosition.getZ() + 4 * MathHelper.sin((float) (angle * Math.PI / 4f)));
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		user.loadFromNBT(tag.getCompound("user"));
		hasHorse = tag.getBoolean("hasHorse");
		if (hasHorse)
			horseId = tag.getUUID("horseId");
		moveTimer = tag.getInt("moveTimer");
		angle = tag.getInt("angle");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("user", user.saveToNBT());
		if (hasHorse)
			tag.putUUID("horseId", horseId);
		tag.putBoolean("hasHorse", hasHorse);
		tag.putFloat("moveTimer", moveTimer);
		tag.putFloat("angle", angle);
		return super.save(tag);
	}

	//	@Nullable
	//	@Override
	//	public SPacketUpdateTileEntity getUpdatePacket()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		int meta = getBlockMetadata();
	//		return new SPacketUpdateTileEntity(levelPosition, meta, nbt);
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
	public boolean AttachHorse(MobEntity horse)
	{
		if (hasHorse)
			return false;
		hasHorse = true;
		this.horse = horse;
		horseId = horse.getUUID();
		user.setTorqueOnFace(Direction.UP, 10);
		user.setSpeedOnFace(Direction.UP, 0);
		setChanged();
		BlockState state2 = Objects.requireNonNull(world).getBlockState(levelPosition);
		world.sendBlockUpdatedd(levelPosition, state2, state2, 3);
		return true;
	}

	public void RemoveHorse()
	{
		if (!hasHorse)
			return;
		hasHorse = false;
		horse = null;
		horseId = null;
		user.setTorqueOnFace(Direction.UP, 0);
		user.setSpeedOnFace(Direction.UP, 0);
		setChanged();
		BlockState state2 = Objects.requireNonNull(world).getBlockState(levelPosition);
		world.sendBlockUpdatedd(levelPosition, state2, state2, 3);
	}
}
