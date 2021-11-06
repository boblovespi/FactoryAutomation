package boblovespi.factoryautomation.datagen.recipe;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

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
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
	{
		// metal storage recipes
		for (int i = 2; i < Metals.values().length; i++)
		{
			ShapelessRecipeBuilder.shapeless(FAItems.nugget.GetItem(Metals.values()[i]), 9)
								  .requires(FATags.ForgeItemTag("ingots/" + Metals.values()[i].name()))
								  .group(Metals.values()[i].name() + "_nugget")
								  .unlockedBy("has_" + Metals.values()[i].name() + "_ingot",
										  has(FATags.ForgeItemTag("ingots/" + Metals.values()[i].name())))
								  .save(consumer, new ResourceLocation(MODID,
										  "ingot_to_nugget_" + Metals.values()[i].name()));

			ShapedRecipeBuilder.shaped(FAItems.ingot.GetItem(Metals.values()[i])).pattern("III")
							   .pattern("III").pattern("III")
							   .define('I', FATags.ForgeItemTag("nuggets/" + Metals.values()[i].name()))
							   .group(Metals.values()[i].name() + "_ingot")
							   .unlockedBy("has_" + Metals.values()[i].name() + "_nugget",
									   has(FATags.ForgeItemTag("nuggets/" + Metals.values()[i].name())))
							   .save(consumer,
									   new ResourceLocation(MODID, "nugget_to_ingot_" + Metals.values()[i].name()));

			ShapelessRecipeBuilder.shapeless(FAItems.ingot.GetItem(Metals.values()[i]), 9)
								  .requires(FATags.ForgeItemTag("storage_blocks/" + Metals.values()[i].name()))
								  .group(Metals.values()[i].name() + "_ingot")
								  .unlockedBy("has_" + Metals.values()[i].name() + "_block", has(FATags
										  .ForgeItemTag("storage_blocks/" + Metals.values()[i].name())))
								  .save(consumer, new ResourceLocation(MODID,
										  "block_to_ingot_" + Metals.values()[i].name()));

			ShapedRecipeBuilder.shaped(FABlocks.metalBlock.GetBlock(Metals.values()[i])).pattern("III")
							   .pattern("III").pattern("III")
							   .define('I', FATags.ForgeItemTag("ingots/" + Metals.values()[i].name()))
							   .group(Metals.values()[i].name() + "_block")
							   .unlockedBy("has_" + Metals.values()[i].name() + "_ingot",
									   has(FATags.ForgeItemTag("ingots/" + Metals.values()[i].name())))
							   .save(consumer,
									   new ResourceLocation(MODID, "ingot_to_block" + Metals.values()[i].name()));
		}

		// tools
		AddToolRecipes(consumer, "bronze", FATags.ForgeItemTag("ingots/bronze"), FATags.ForgeItemTag("rods/wooden"),
				FAItems.bronzePickaxe, FAItems.bronzeAxe, FAItems.bronzeSword, FAItems.bronzeHoe, FAItems.bronzeShovel);

		AddToolRecipes(consumer, "steel", FATags.ForgeItemTag("ingots/steel"), FATags.ForgeItemTag("rods/wooden"),
				FAItems.steelPickaxe, FAItems.steelAxe, FAItems.steelSword, FAItems.steelHoe, FAItems.steelShovel);

		AddToolRecipes(consumer, "copper", FATags.ForgeItemTag("ingots/copper"), FATags.ForgeItemTag("rods/wooden"),
				FAItems.copperPickaxe, FAItems.copperAxe, FAItems.copperSword, FAItems.copperHoe, FAItems.copperShovel);

	}

	private void AddToolRecipes(Consumer<IFinishedRecipe> consumer, String materialName, @Nonnull ITag<Item> ingot,
			@Nonnull ITag<Item> stick, @Nullable FAItem pickaxe, @Nullable FAItem axe, @Nullable FAItem sword,
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
