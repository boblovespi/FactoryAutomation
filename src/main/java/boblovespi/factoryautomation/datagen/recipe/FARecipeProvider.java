package boblovespi.factoryautomation.datagen.recipe;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.ores.OreForms;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class FARecipeProvider extends RecipeProvider
{
	public FARecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		// metal storage recipes
		for (int i = 3; i < Metals.values().length; i++)
		{
			ShapelessRecipeBuilder.shapeless(FAItems.nugget.GetItem(Metals.values()[i]), 9)
								  .requires(FATags.CreateForgeItemTag("ingots/" + Metals.values()[i].toString()))
								  .group(Metals.values()[i].toString() + "_nugget")
								  .unlockedBy("has_" + Metals.values()[i].toString() + "_ingot",
										  has(FATags.CreateForgeItemTag("ingots/" + Metals.values()[i].toString())))
								  .save(consumer, new ResourceLocation(MODID,
										  "ingot_to_nugget_" + Metals.values()[i].toString()));

			ShapedRecipeBuilder.shaped(FAItems.ingot.GetItem(Metals.values()[i])).pattern("III")
							   .pattern("III").pattern("III")
							   .define('I', FATags.CreateForgeItemTag("nuggets/" + Metals.values()[i].toString()))
							   .group(Metals.values()[i].toString() + "_ingot")
							   .unlockedBy("has_" + Metals.values()[i].toString() + "_nugget",
									   has(FATags.CreateForgeItemTag("nuggets/" + Metals.values()[i].toString())))
							   .save(consumer,
									   new ResourceLocation(MODID, "nugget_to_ingot_" + Metals.values()[i].toString()));

			ShapelessRecipeBuilder.shapeless(FAItems.ingot.GetItem(Metals.values()[i]), 9)
								  .requires(FATags.CreateForgeItemTag("storage_blocks/" + Metals.values()[i].toString()))
								  .group(Metals.values()[i].toString() + "_ingot")
								  .unlockedBy("has_" + Metals.values()[i].toString() + "_block", has(FATags
										  .CreateForgeItemTag("storage_blocks/" + Metals.values()[i].toString())))
								  .save(consumer, new ResourceLocation(MODID,
										  "block_to_ingot_" + Metals.values()[i].toString()));

			ShapedRecipeBuilder.shaped(FABlocks.metalBlock.GetBlock(Metals.values()[i])).pattern("III")
							   .pattern("III").pattern("III")
							   .define('I', FATags.CreateForgeItemTag("ingots/" + Metals.values()[i].toString()))
							   .group(Metals.values()[i].toString() + "_block")
							   .unlockedBy("has_" + Metals.values()[i].toString() + "_ingot",
									   has(FATags.CreateForgeItemTag("ingots/" + Metals.values()[i].toString())))
							   .save(consumer,
									   new ResourceLocation(MODID, "ingot_to_block" + Metals.values()[i].toString()));
		}

		// tools
		AddToolRecipes(consumer, "bronze", FATags.CreateForgeItemTag("ingots/bronze"), FATags.CreateForgeItemTag("rods/wooden"),
				FAItems.bronzePickaxe, FAItems.bronzeAxe, FAItems.bronzeSword, FAItems.bronzeHoe, FAItems.bronzeShovel);

		AddToolRecipes(consumer, "steel", FATags.CreateForgeItemTag("ingots/steel"), FATags.CreateForgeItemTag("rods/wooden"),
				FAItems.steelPickaxe, FAItems.steelAxe, FAItems.steelSword, FAItems.steelHoe, FAItems.steelShovel);

		AddToolRecipes(consumer, "copper", FATags.CreateForgeItemTag("ingots/copper"), FATags.CreateForgeItemTag("rods/wooden"),
				FAItems.copperPickaxe, FAItems.copperAxe, FAItems.copperSword, FAItems.copperHoe, FAItems.copperShovel);

		// raw ore blocks
		AddRawBlock(consumer, FABlocks.limoniteRawBlock, FATags.CreateForgeItemTag("raw_ores/limonite"), FAItems.processedLimonite.GetItem(OreForms.CHUNK));
		AddRawBlock(consumer, FABlocks.cassiteriteRawBlock, FATags.CreateForgeItemTag("raw_ores/tin"), FAItems.processedCassiterite.GetItem(OreForms.CHUNK));
	}

	private void AddToolRecipes(Consumer<FinishedRecipe> consumer, String materialName, @Nonnull TagKey<Item> ingot,
			@Nonnull TagKey<Item> stick, @Nullable Item pickaxe, @Nullable Item axe, @Nullable Item sword,
			@Nullable Item hoe, @Nullable Item shovel)
	{
		if (pickaxe != null)
		{
			ShapedRecipeBuilder.shaped(pickaxe).pattern("iii").pattern(" s ").pattern(" s ")
							   .define('i', ingot).define('s', stick).unlockedBy("has_" + materialName, has(ingot))
							   .save(consumer, new ResourceLocation(MODID, materialName + "_pickaxe"));
		}
		if (axe != null)
		{
			ShapedRecipeBuilder.shaped(axe).pattern("ii").pattern("is").pattern(" s").define('i', ingot)
							   .define('s', stick).unlockedBy("has_" + materialName, has(ingot))
							   .save(consumer, new ResourceLocation(MODID, materialName + "_axe"));
		}
		if (hoe != null)
		{
			ShapedRecipeBuilder.shaped(hoe).pattern("ii").pattern(" s").pattern(" s").define('i', ingot)
							   .define('s', stick).unlockedBy("has_" + materialName, has(ingot))
							   .save(consumer, new ResourceLocation(MODID, materialName + "_hoe"));
		}
		if (sword != null)
		{
			ShapedRecipeBuilder.shaped(sword).pattern("i").pattern("i").pattern("s").define('i', ingot)
							   .define('s', stick).unlockedBy("has_" + materialName, has(ingot))
							   .save(consumer, new ResourceLocation(MODID, materialName + "_sword"));
		}
		if (shovel != null)
		{
			ShapedRecipeBuilder.shaped(shovel).pattern("i").pattern("s").pattern("s").define('i', ingot)
							   .define('s', stick).unlockedBy("has_" + materialName, has(ingot))
							   .save(consumer, new ResourceLocation(MODID, materialName + "_shovel"));
		}
	}

	private void AddRawBlock(Consumer<FinishedRecipe> consumer, Block rawOre, TagKey<Item> fromChunk, Item toChunk)
	{
		ShapelessRecipeBuilder.shapeless(toChunk, 9).requires(rawOre).group(toChunk.toString())
							  .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(rawOre).getPath(), has(rawOre))
							  .save(consumer, new ResourceLocation(MODID,
									  ForgeRegistries.BLOCKS.getKey(rawOre).getPath() + "_to_" + toChunk));

		ShapedRecipeBuilder.shaped(rawOre).pattern("III").pattern("III").pattern("III").define('I', fromChunk)
						   .group(ForgeRegistries.BLOCKS.getKey(rawOre).getPath())
						   .unlockedBy("has_" + toChunk, has(fromChunk)).save(consumer,
								   new ResourceLocation(MODID, toChunk + "_to_" + ForgeRegistries.BLOCKS.getKey(rawOre).getPath()));
	}
}
