package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.Collection;

/**
 * Created by Willi on 11/29/2019.
 * Todo: translate getOrCreate to 1.16.5
 */
public class FATags
{
	// public static final Tag<Item> INGOT_BRONZE = itemTag("ingots/bronze");

	public static Tag<Item> ForgeItemTag(String name)
	{
		return ItemTags.getAllTags().getOrCreate(new ResourceLocation("forge", name));
	}

	public static Tag<Item> FAItemTag(String name)
	{
		return ItemTags.getAllTags().getOrCreate(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static Tag<Block> FABlockTag(String name)
	{
		return BlockTags.getAllTags().getOrCreate(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static Tag<Block> ForgeBlockTag(String name)
	{
		return BlockTags.getAllTags().getOrCreate(new ResourceLocation("forge", name));
	}
}
