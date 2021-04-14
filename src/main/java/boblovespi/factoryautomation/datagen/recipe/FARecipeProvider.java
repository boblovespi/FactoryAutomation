package boblovespi.factoryautomation.datagen.recipe;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
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
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		// metal storage recipes
		for (int i = 2; i < Metals.values().length; i++)
		{
			ShapelessRecipeBuilder.shapelessRecipe(FAItems.nugget.GetItem(Metals.values()[i]), 9)
								  .addIngredient(FATags.ForgeItemTag("ingots/" + Metals.values()[i].getString()))
								  .setGroup(Metals.values()[i].getString() + "_nugget")
								  .addCriterion("has_" + Metals.values()[i].getString() + "_ingot",
										  hasItem(FATags.ForgeItemTag("ingots/" + Metals.values()[i].getString())))
								  .build(consumer, new ResourceLocation(MODID,
										  "ingot_to_nugget_" + Metals.values()[i].getString()));

			ShapedRecipeBuilder.shapedRecipe(FAItems.ingot.GetItem(Metals.values()[i])).patternLine("III")
							   .patternLine("III").patternLine("III")
							   .key('I', FATags.ForgeItemTag("nuggets/" + Metals.values()[i].getString()))
							   .setGroup(Metals.values()[i].getString() + "_ingot")
							   .addCriterion("has_" + Metals.values()[i].getString() + "_nugget",
									   hasItem(FATags.ForgeItemTag("nuggets/" + Metals.values()[i].getString())))
							   .build(consumer,
									   new ResourceLocation(MODID, "nugget_to_ingot_" + Metals.values()[i].getString()));

			ShapelessRecipeBuilder.shapelessRecipe(FAItems.ingot.GetItem(Metals.values()[i]), 9)
								  .addIngredient(FATags.ForgeItemTag("storage_blocks/" + Metals.values()[i].getString()))
								  .setGroup(Metals.values()[i].getString() + "_ingot")
								  .addCriterion("has_" + Metals.values()[i].getString() + "_block", hasItem(FATags
										  .ForgeItemTag("storage_blocks/" + Metals.values()[i].getString())))
								  .build(consumer, new ResourceLocation(MODID,
										  "block_to_ingot_" + Metals.values()[i].getString()));

			ShapedRecipeBuilder.shapedRecipe(FABlocks.metalBlock.GetBlock(Metals.values()[i])).patternLine("III")
							   .patternLine("III").patternLine("III")
							   .key('I', FATags.ForgeItemTag("ingots/" + Metals.values()[i].getString()))
							   .setGroup(Metals.values()[i].getString() + "_block")
							   .addCriterion("has_" + Metals.values()[i].getString() + "_ingot",
									   hasItem(FATags.ForgeItemTag("ingots/" + Metals.values()[i].getString())))
							   .build(consumer,
									   new ResourceLocation(MODID, "ingot_to_block" + Metals.values()[i].getString()));
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
			ShapedRecipeBuilder.shapedRecipe(pickaxe).patternLine("iii").patternLine(" s ").patternLine(" s ")
							   .key('i', ingot).key('s', stick).addCriterion("has_" + materialName, hasItem(ingot))
							   .build(consumer, new ResourceLocation(MODID, materialName + "_pickaxe"));
		}
		if (axe != null)
		{
			ShapedRecipeBuilder.shapedRecipe(axe).patternLine("ii").patternLine("is").patternLine(" s").key('i', ingot)
							   .key('s', stick).addCriterion("has_" + materialName, hasItem(ingot))
							   .build(consumer, new ResourceLocation(MODID, materialName + "_axe"));
		}
		if (hoe != null)
		{
			ShapedRecipeBuilder.shapedRecipe(hoe).patternLine("ii").patternLine(" s").patternLine(" s").key('i', ingot)
							   .key('s', stick).addCriterion("has_" + materialName, hasItem(ingot))
							   .build(consumer, new ResourceLocation(MODID, materialName + "_hoe"));
		}
		if (sword != null)
		{
			ShapedRecipeBuilder.shapedRecipe(sword).patternLine("i").patternLine("i").patternLine("s").key('i', ingot)
							   .key('s', stick).addCriterion("has_" + materialName, hasItem(ingot))
							   .build(consumer, new ResourceLocation(MODID, materialName + "_sword"));
		}
		if (shovel != null)
		{
			ShapedRecipeBuilder.shapedRecipe(shovel).patternLine("i").patternLine("s").patternLine("s").key('i', ingot)
							   .key('s', stick).addCriterion("has_" + materialName, hasItem(ingot))
							   .build(consumer, new ResourceLocation(MODID, materialName + "_shovel"));
		}
	}
}
