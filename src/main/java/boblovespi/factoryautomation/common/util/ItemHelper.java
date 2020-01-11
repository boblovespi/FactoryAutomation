package boblovespi.factoryautomation.common.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Willi on 8/14/2018.
 */
public class ItemHelper
{
	public static void PutItemsInInventoryOrDrop(PlayerEntity player, ItemStack stack, World world)
	{
		if (!player.addItemStackToInventory(stack.copy()))
			world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, stack.copy()));
	}

	public static void DamageItem(ItemStack stack)
	{
		DamageItem(stack, 1);
	}

	public static void DamageItem(ItemStack stack, int amount)
	{
		DamageItem(stack, amount, null);
	}

	public static void DamageItem(ItemStack stack, int amount, EntityPlayerMP damager)
	{
		boolean b = stack.attemptDamageItem(1, damager == null ? Randoms.MAIN.r : damager.getRNG(), damager);
		if (b)
			stack.shrink(amount);
	}

	public static String GetItemID(Item item, int meta)
	{
		return item.getRegistryName().toString() + ":" + meta;
	}

	public static String GetItemID(ItemStack input)
	{
		return GetItemID(input.getItem(), input.getMetadata());
	}
}
