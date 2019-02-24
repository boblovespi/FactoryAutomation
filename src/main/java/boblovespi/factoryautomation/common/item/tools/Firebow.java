package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FABaseItem;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Created by Willi on 2/15/2019.
 */
public class Firebow extends FABaseItem
{
	public Firebow()
	{
		super("firebow", FACreativeTabs.primitive);
		setMaxDamage(15);
		setMaxStackSize(1);
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		player.setActiveHand(hand);
		return EnumActionResult.PASS;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase living)
	{
		if (!(living instanceof EntityPlayer))
			return stack;
		EntityPlayer player = (EntityPlayer) living;
		RayTraceResult rayTrace = rayTrace(world, player, true);
		if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK)
			return stack;
		BlockPos pos = rayTrace.getBlockPos();
		EnumFacing facing = rayTrace.sideHit;
		pos = pos.offset(facing);

		if (!player.canPlayerEdit(pos, facing, stack))
		{
			return stack;
		} else
		{
			if (world.isAirBlock(pos))
			{
				world.playSound(
						player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F,
						itemRand.nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
			}

			if (player instanceof EntityPlayerMP)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
			}

			stack.damageItem(1, player);
			return stack;
		}
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 60;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
}
