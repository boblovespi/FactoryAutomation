package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Willi on 11/29/2019.
 * Todo: translate getOrCreate to 1.16.5
 */
public class FATags
{
	// public static final TagKey<Item> INGOT_BRONZE = itemTag("ingots/bronze");
	public static final TagKey<Block> NEEDS_FLINT_TOOL = CreateFABlockTag("needs_flint_tool");
	public static final TagKey<Block> NEEDS_COPPER_TOOL = CreateFABlockTag("needs_copper_tool");
	public static final TagKey<Block> NEEDS_BRONZE_TOOL = CreateFABlockTag("needs_bronze_tool");
	public static final TagKey<Block> NEEDS_STEEL_TOOL = CreateFABlockTag("needs_steel_tool");
	public static final TagKey<Block> HAMMER_TOOL = CreateFABlockTag("mineable/hammer");
	public static final TagKey<Block> NOTHING_TOOL = CreateFABlockTag("mineable/nothing");

	public static TagKey<Item> ForgeItemTag(String name)
	{
		return CreateForgeItemTag(name);
	}

	public static TagKey<Item> FAItemTag(String name)
	{
		return CreateFAItemTag(name);
	}

	public static TagKey<Block> FABlockTag(String name)
	{
		return CreateFABlockTag(name);
	}

	public static TagKey<Block> ForgeBlockTag(String name)
	{
		return CreateForgeBlockTag(name);
	}

	public static TagKey<Item> CreateForgeItemTag(String name)
	{
		return ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", name.toLowerCase()));
	}

	public static TagKey<Item> CreateFAItemTag(String name)
	{
		return ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static TagKey<Block> CreateFABlockTag(String name)
	{
		return ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static TagKey<Block> CreateForgeBlockTag(String name)
	{
		return ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation("forge", name.toLowerCase()));
	}

	public static <T> TagKey<T> FATag(ResourceKey<? extends Registry<T>> key, String name)
	{
		return TagKey.create(key, new ResourceLocation(FactoryAutomation.MODID, name.toLowerCase()));
	}

	public static boolean Contains(TagKey<Block> tag, Block block)
	{
		return ForgeRegistries.BLOCKS.tags().getTag(tag).contains(block);
	}

	public static boolean Contains(TagKey<Item> tag, Item item)
	{
		return ForgeRegistries.ITEMS.tags().getTag(tag).contains(item);
	}

	public static boolean Contains(TagKey<Fluid> tag, Fluid fluid)
	{
		return ForgeRegistries.FLUIDS.tags().getTag(tag).contains(fluid);
	}

	public static boolean Contains(TagKey<Biome> tag, Biome biome)
	{
		return ForgeRegistries.BIOMES.tags().getTag(tag).contains(biome);
	}
}
