package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.energy.mechanical.MechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.mechanical.LeatherBellows.FACING;
import static boblovespi.factoryautomation.common.util.TEHelper.GetUser;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

/**
 * Created by Willi on 5/11/2019.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TELeatherBellows extends BlockEntity implements ITickable, IBellowsTE, IAnimatable
{
	private final static float secondsPerBlow = 2.5f; // time per blow at 100% speed
	private final static float maxSpeed = 20f; // in rad/s
	private final static float maxCounter = 100f;
	private final static float torqueFactor = maxSpeed; // amount of torque per speed
	private final MechanicalUser mechanicalUser;
	private float counter = 0; // counter to next blow, ranges from 0 to maxCounter (=100), 0 = blow
	private int c2 = 0; // counter to update the mechanical user
	private float lerp = 0;
	private int blowCounter = 0; // counter for geckolib animation; > 0 is animating
	private boolean firstTick = true;
	private AnimationFactory factory;

	public TELeatherBellows(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teLeatherBellows.get(), pos, state);
		mechanicalUser = new MechanicalUser();
		factory = GeckoLibUtil.createFactory(this);
	}

	public void FirstLoad()
	{
		Direction dir = getBlockState().getValue(FACING);
		mechanicalUser.SetSides(EnumSet.of(dir.getOpposite()));
		firstTick = false;
	}

	public void Blow()
	{
		Direction facing = getBlockState().getValue(FACING);
		BlockEntity te = Objects.requireNonNull(level).getBlockEntity(worldPosition.relative(facing));
		if (te == null)
			return;
		var bellows = te.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());
		bellows.ifPresent(n -> n.Blow(Efficiency(), (int) (secondsPerBlow * EnergyConstants.TICKS_IN_SECOND)));
		level.playSound(null, worldPosition, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.BLOCKS, 0.8f, 1.5f);
		blowCounter = 20;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	private boolean FastEnoughForTorque()
	{
		var efficiency = 2 * (Efficiency() - 0.5f);
		var speed = mechanicalUser.GetSpeed();
		return efficiency * torqueFactor <= speed;
	}

	private float CounterModification()
	{
		if (FastEnoughForTorque())
		{
			var speed = mechanicalUser.GetSpeed() / maxSpeed;
			var timeInTicks = secondsPerBlow * EnergyConstants.TICKS_IN_SECOND * speed;
			return maxCounter / timeInTicks;
		}
		return 0;
	}

	private float Efficiency()
	{
		return Mth.clamp(mechanicalUser.GetTorque() / 30f, 0.5f, 1f);
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
		{
			counter -= CounterModification();
			if (counter <= 0)
			{
				counter = maxCounter;
			}
			lerp = Math.abs(2 * (counter / maxCounter) - 1);
			if (blowCounter > 0)
				blowCounter--;
			return;
		}
		if (firstTick)
			FirstLoad();

		counter -= CounterModification();
		if (blowCounter > 0)
			blowCounter--;
		if (counter <= 0)
		{
			counter = maxCounter;
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
			}
			else
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
		blowCounter = tag.getInt("blowCounter");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.put("mechanicalUser", mechanicalUser.WriteToNBT());
		tag.putFloat("counter", counter);
		tag.putInt("blowCounter", blowCounter);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
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

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, event -> {
			if (blowCounter > 0)
			{
				event.getController()
					 .setAnimation(new AnimationBuilder().addAnimation("animation.model.pumping_air", LOOP));
				return PlayState.CONTINUE;
			}
			else
				return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
}
