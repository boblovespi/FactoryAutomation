package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Willi on 11/29/2019.
 * Todo: translate getOrCreate to 1.16.5
 */
public class FATags
{
	// public static final Tag<Item> INGOT_BRONZE = itemTag("ingots/bronze");

	public static ITag<Item> ForgeItemTag(String name)
	{
		return ItemTags.createOptional(new ResourceLocation("forge", name));
	}

	public static ITag<Item> FAItemTag(String name)
	{
		return ItemTags.createOptional(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag<Block> FABlockTag(String name)
	{
		return BlockTags.createOptional(new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag<Block> ForgeBlockTag(String name)
	{
		return BlockTags.createOptional(new ResourceLocation("forge", name));
	}

	public static ITag.INamedTag<Item> CreateForgeItemTag(String name)
	{
		return ForgeTagHandler.makeWrapperTag(ForgeRegistries.ITEMS, new ResourceLocation("forge", name));
	}

	public static ITag.INamedTag<Item> CreateFAItemTag(String name)
	{
		return ForgeTagHandler.makeWrapperTag(ForgeRegistries.ITEMS, new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag.INamedTag<Block> CreateFABlockTag(String name)
	{
		return ForgeTagHandler.makeWrapperTag(ForgeRegistries.BLOCKS, new ResourceLocation(FactoryAutomation.MODID, name));
	}

	public static ITag.INamedTag<Block> CreateForgeBlockTag(String name)
	{
		return ForgeTagHandler.makeWrapperTag(ForgeRegistries.BLOCKS, new ResourceLocation("forge", name));
	}
}
