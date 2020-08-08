package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
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

/**
 * Created by Willi on 2/15/2019.
 */
public class Firebow extends FABaseItem
{
	public Firebow()
	{
		super("firebow", new Properties().group(FAItemGroups.primitive).maxDamage(15).maxStackSize(1));
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
	public ActionResultType onItemUse(ItemUseContext context)
	{
		context.getPlayer().setActiveHand(context.getHand());
		return ActionResultType.PASS;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		player.setActiveHand(hand);
		return ActionResult.resultPass(player.getHeldItem(hand));
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity living)
	{
		if (!(living instanceof PlayerEntity))
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		RayTraceResult rayTrace = rayTrace(world, player, RayTraceContext.FluidMode.NONE);
		if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK)
			return stack;
		BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTrace;
		BlockPos pos = blockRayTrace.getPos();
		Direction facing = blockRayTrace.getFace();
		pos = pos.offset(facing);

		if (!player.canPlayerEdit(pos, facing, stack))
		{
			return stack;
		} else
		{
			if (world.isAirBlock(pos))
			{
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F,
						random.nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
			}

			if (player instanceof ServerPlayerEntity)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
			}

			stack.damageItem(1, player, n -> n.sendBreakAnimation(player.getActiveHand()));
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
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}
}
