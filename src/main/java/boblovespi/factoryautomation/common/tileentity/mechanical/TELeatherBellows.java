package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.mechanical.LeatherBellows.FACING;
import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;

/**
 * Created by Willi on 5/11/2019.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TELeatherBellows extends BlockEntity implements ITickable, IBellowsTE
{
	private float counter = 0;
	private int c2 = 0;
	private final MechanicalUser mechanicalUser;
	private float lerp = 0;
	private boolean firstTick = true;

	public TELeatherBellows(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teLeatherBellows, pos, state);
		mechanicalUser = new MechanicalUser();
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().getValue(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.getOpposite()));
		firstTick = false;
	}

	public void Blow()
	{
		Direction facing = getBlockState().getValue(PaperBellows.FACING);
		BlockEntity te = Objects.requireNonNull(level).getBlockEntity(worldPosition.relative(facing));
		if (te == null)
			return;
		LazyOptional<IBellowsable> capability = te
				.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		capability.ifPresent(n -> n.Blow(Mth.clamp(mechanicalUser.GetTorque() / 30f, 0.5f, 1), 50));
		level.playSound(null, worldPosition, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.BLOCKS, 0.8f, 1.5f);
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
		{
			counter -= mechanicalUser.GetSpeed() / 10f;
			if (counter <= 0)
			{
				counter = 100;
			}
			lerp = Math.abs(2 * (counter / 100f) - 1);
			return;
		}
		if (firstTick)
			FirstLoad();

		counter -= mechanicalUser.GetSpeed() / 10f;
		if (counter <= 0)
		{
			counter = 100;
			Blow();
		}
		c2--;
		if (c2 <= 0)
		{
			Direction facing = getBlockState().getValue(FACING);
			BlockEntity te = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));
			if (TEHelper.IsMechanicalFace(te, facing))
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), GetUser(te, facing).GetSpeedOnFace(facing));
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), GetUser(te, facing).GetTorqueOnFace(facing));
			} else
			{
				mechanicalUser.SetSpeedOnFace(facing.getOpposite(), 0);
				mechanicalUser.SetTorqueOnFace(facing.getOpposite(), 0);
			}

			setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			c2 = 4;
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		mechanicalUser.ReadFromNBT(tag.getCompound("mechanicalUser"));
		counter = tag.getFloat("counter");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.putFloat("counter", counter);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) mechanicalUser);
		return super.getCapability(capability, facing);
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
