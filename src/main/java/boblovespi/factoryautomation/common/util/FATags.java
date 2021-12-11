package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Willi on 11/29/2019.
 * Todo: translate getOrCreate to 1.16.5
 */
public class FATags
{
	// public static final Tag<Item> INGOT_BRONZE = itemTag("ingots/bronze");
	public static final Tag.Named<Block> NEEDS_FLINT_TOOL = CreateFABlockTag("needs_flint_tool");
	public static final Tag.Named<Block> NEEDS_COPPER_TOOL = CreateFABlockTag("needs_copper_tool");
	public static final Tag.Named<Block> NEEDS_BRONZE_TOOL = CreateFABlockTag("needs_bronze_tool");
	public static final Tag.Named<Block> NEEDS_STEEL_TOOL = CreateFABlockTag("needs_steel_tool");
	public static final Tag.Named<Block> HAMMER_TOOL = CreateFABlockTag("mineable/hammer");
	public static final Tag.Named<Block> NOTHING_TOOL = CreateFABlockTag("mineable/nothing");

	public static Tag<Item> ForgeItemTag(String name)
	{
		return ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", name.toLowerCase()));
	}

	public static Tag<Item> FAItemTag(String name)
	{
		return ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static Tag<Block> FABlockTag(String name)
	{
		return BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static Tag<Block> ForgeBlockTag(String name)
	{
		return BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", name.toLowerCase()));
	}

	public static Tag.Named<Item> CreateForgeItemTag(String name)
	{
		return ForgeTagHandler.createOptionalTag(ForgeRegistries.ITEMS, new ResourceLocation("forge", name.toLowerCase()));
	}

	public static Tag.Named<Item> CreateFAItemTag(String name)
	{
		return ForgeTagHandler.createOptionalTag(ForgeRegistries.ITEMS, new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static Tag.Named<Block> CreateFABlockTag(String name)
	{
		return ForgeTagHandler.createOptionalTag(ForgeRegistries.BLOCKS, new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static Tag.Named<Block> CreateForgeBlockTag(String name)
	{
		return ForgeTagHandler.createOptionalTag(ForgeRegistries.BLOCKS, new ResourceLocation("forge", name.toLowerCase()));
	}
}
