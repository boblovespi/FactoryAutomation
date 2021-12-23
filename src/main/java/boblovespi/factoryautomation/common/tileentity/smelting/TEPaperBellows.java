package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.misc.IBellowsable;
import boblovespi.factoryautomation.client.tesr.IBellowsTE;
import boblovespi.factoryautomation.common.block.processing.PaperBellows;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Objects;

/**
 * Created by Willi on 5/5/2019.
 */
public class TEPaperBellows extends BlockEntity implements ITickable, IBellowsTE, IAnimatable
{
	private float lerp = 0;
	private AnimationFactory factory;

	public TEPaperBellows(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.tePaperBellows, pos, state);
		factory = new AnimationFactory(this);
	}

	public void Blow()
	{
		if (!Objects.requireNonNull(level).isClientSide)
		{
			Direction facing = level.getBlockState(worldPosition).getValue(PaperBellows.FACING);
			BlockEntity te = level.getBlockEntity(worldPosition.relative(facing));
			if (te == null)
				return;
			LazyOptional<IBellowsable> capability = te
					.getCapability(CapabilityBellowsUser.BELLOWS_USER_CAPABILITY, facing.getOpposite());

			capability.ifPresent(n -> n.Blow(0.75f, 400));
		} else
		{
			lerp = 1;
		}
	}

	@Override
	public float GetLerp()
	{
		return Math.abs(2 * lerp - 1);
	}

	@Override
	public float GetLerpSpeed()
	{
		return (lerp > 0.5f ? -1 : 1) / 80f;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (!Objects.requireNonNull(level).isClientSide)
			return;
		if (lerp > 0)
		{
			lerp -= 1 / 20f;
		} else
			lerp = 0;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, event -> {
			event.getController().transitionLengthTicks = 0;
			if (lerp > 0)
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.pumping_air", true));
				return PlayState.CONTINUE;
			}
			else return PlayState.STOP;
		}));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
}
