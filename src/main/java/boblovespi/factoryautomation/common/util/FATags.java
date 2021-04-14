package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

/**
 * Created by Willi on 11/29/2019.
 */
public class FATags
{
	// public static final Tag<Item> INGOT_BRONZE = itemTag("ingots/bronze");

	public static ITag<Item> ForgeItemTag(String name)
	{
		return ItemTags.getCollection().get(new ResourceLocation("forge", name));
	}

	public static ITag<Item> FAItemTag(String name)
	{
		return ItemTags.getCollection().get(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag<Block> FABlockTag(String name)
	{
		return BlockTags.getCollection().get(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag<Block> ForgeBlockTag(String name)
	{
		return BlockTags.getCollection().get(new ResourceLocation("forge", name));
	}
}
