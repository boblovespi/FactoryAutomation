package boblovespi.factoryautomation.common.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Created by Willi on 8/14/2018.
 */
public class ItemHelper
{
	public static void PutItemsInInventoryOrDrop(Player player, ItemStack stack, Level level)
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

	public static void DamageItem(ItemStack stack, int amount, ServerPlayer damager)
	{
		boolean b = stack.hurt(1, damager == null ? Randoms.MAIN.r : damager.getRandom(), damager);
		if (b)
			stack.shrink(amount);
	}
}
