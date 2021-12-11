package boblovespi.factoryautomation.datagen.recipe;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.*;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

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
		for (int i = 2; i < Metals.values().length; i++)
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

	}

	private void AddToolRecipes(Consumer<FinishedRecipe> consumer, String materialName, @Nonnull Tag<Item> ingot,
			@Nonnull Tag<Item> stick, @Nullable FAItem pickaxe, @Nullable FAItem axe, @Nullable FAItem sword,
			@Nullable FAItem hoe, @Nullable FAItem shovel)
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
}
