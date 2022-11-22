package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 2/15/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Firebow extends FABaseItem
{
	public Firebow()
	{
		super("firebow", new Properties().tab(FAItemGroups.primitive).durability(15));
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Objects.requireNonNull(context.getPlayer()).startUsingItem(context.getHand());
		return InteractionResult.PASS;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		player.startUsingItem(hand);
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living)
	{
		if (!(living instanceof Player player))
			return stack;
		BlockHitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

		// Todo: remove rayTrace null check, it shouldn't be null anyways.
		if (rayTrace == null || rayTrace.getType() != HitResult.Type.BLOCK)
			return stack;
		BlockPos pos = rayTrace.getBlockPos();
		Direction facing = rayTrace.getDirection();
		pos = pos.relative(facing);

		if (!player.mayUseItemAt(pos, facing, stack))
		{
			return stack;
		} else
		{
			if (world.isEmptyBlock(pos))
			{
				world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
						world.random.nextFloat() * 0.4F + 0.8F);
				world.setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
			}

			if (player instanceof ServerPlayer)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
			}

			stack.hurtAndBreak(1, player, n -> n.broadcastBreakEvent(player.getUsedItemHand()));
			return stack;
		}
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 60;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.BOW;
	}
}
