package boblovespi.factoryautomation.common.crafting;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber

public class RecipeHandler
{
	private static IRecipe concrete;

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerIRecipes(RegistryEvent.Register<IRecipe> event)
	{
		concrete = new ShapelessRecipes(FactoryAutomation.MODID,
				new ItemStack(FABlocks.concrete.ToBlock()), NonNullList
				.from(Ingredient.fromItem(FAItems.slag.ToItem()),
						Ingredient.fromItem(Items.WATER_BUCKET), Ingredient
								.fromItem(Item.getItemFromBlock(Blocks.SAND))));
		concrete.setRegistryName(
				new ResourceLocation(FactoryAutomation.MODID, "concrete"));
		event.getRegistry().register(concrete);
	}

	public static void RegisterCraftingRecipes()
	{
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(FactoryAutomation.MODID, "concrete"),
				new ResourceLocation(""),
				new ItemStack(FABlocks.concrete.ToBlock()),
				Ingredient.fromItem(FAItems.slag.ToItem()),
				Ingredient.fromItem(Items.WATER_BUCKET));
		Log.getLogger().info("Registered Recipes");
		//Smelting
		Log.getLogger().info("Registered Smelting Recipes");
		GameRegistry.addSmelting(new ItemStack(FAItems.slag.ToItem()),
				new ItemStack(FABlocks.concrete.ToBlock()), 0.7f);

	}

}
