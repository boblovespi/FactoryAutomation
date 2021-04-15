package boblovespi.factoryautomation.common.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Willi on 8/14/2018.
 */
public class ItemHelper
{
	public static void PutItemsInInventoryOrDrop(PlayerEntity player, ItemStack stack, World level)
	{
		if (!player.addItem(stack.copy()))
			level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack.copy()));
	}

	public static void DamageItem(ItemStack stack)
	{
		DamageItem(stack, 1);
	}

	public static void DamageItem(ItemStack stack, int amount)
	{
		DamageItem(stack, amount, null);
	}

	public static void DamageItem(ItemStack stack, int amount, ServerPlayerEntity damager)
	{
		boolean b = stack.hurt(1, damager == null ? Randoms.MAIN.r : damager.getRandom(), damager);
		if (b)
			stack.shrink(amount);
	}
}
