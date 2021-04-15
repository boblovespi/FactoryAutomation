package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "tools/" + RegistryName();
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		Objects.requireNonNull(context.getPlayer()).startUsingItem(context.getHand());
		return ActionResultType.PASS;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		player.startUsingItem(hand);
		return ActionResult.pass(player.getItemInHand(hand));
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
	{
		if (!(living instanceof PlayerEntity))
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		BlockRayTraceResult rayTrace = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);

		// Todo: remove rayTrace null check, it shouldn't be null anyways.
		if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK)
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
				world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F,
						random.nextFloat() * 0.4F + 0.8F);
				world.setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
			}

			if (player instanceof ServerPlayerEntity)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
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
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.BOW;
	}
}
