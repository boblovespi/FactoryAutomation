package boblovespi.factoryautomation.common.crafting;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber

public class RecipeHandler
{
	public static List<IRecipe> recipes;
	private static IRecipe concrete;

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerIRecipes(RegistryEvent.Register<IRecipe> event)
	{
		concrete = new ShapelessOreRecipe(
				new ResourceLocation(FactoryAutomation.MODID, "concrete"),
				FABlocks.concrete.ToBlock(), FAItems.slag.ToItem(),
				Items.WATER_BUCKET, Blocks.SAND).setRegistryName(
				new ResourceLocation(FactoryAutomation.MODID, "concrete"));

		recipes = new ArrayList<>();
		recipes.add(concrete);

		for (int i = 2; i < Metals.values().length; i++)
		{
			IRecipe ingotToNugget = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
							"ingot_to_nugget_" + Metals.values()[i].getName()),
					new ItemStack(FAItems.nugget.ToItem(), 9, i), "I", 'I',
					new ItemStack(FAItems.ingot.ToItem(), 1, i));

			ingotToNugget.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
							"ingot_to_nugget_" + Metals.values()[i].getName()));

			IRecipe nuggetToIngot = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
							"nugget_to_ingot_" + Metals.values()[i].getName()),
					new ItemStack(FAItems.ingot.ToItem(), 1, i), "NNN", "NNN",
					"NNN", 'N', new ItemStack(FAItems.nugget.ToItem(), 1, i));

			nuggetToIngot.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
							"nugget_to_ingot_" + Metals.values()[i].getName()));

			recipes.add(ingotToNugget);
			recipes.add(nuggetToIngot);
		}

		event.getRegistry().registerAll(recipes.toArray(new IRecipe[] {}));
	}

	public static void RegisterCraftingRecipes()
	{
		//		GameRegistry.addShapelessRecipe(
		//				new ResourceLocation(FactoryAutomation.MODID, "concrete"),
		//				new ResourceLocation(""),
		//				new ItemStack(FABlocks.concrete.ToBlock()),
		//				Ingredient.fromItem(FAItems.slag.ToItem()),
		//				Ingredient.fromItem(Items.WATER_BUCKET));
		//		Log.getLogger().info("Registered Recipes");
		//		//Smelting
		//		Log.getLogger().info("Registered Smelting Recipes");
		//		GameRegistry.addSmelting(new ItemStack(FAItems.slag.ToItem()),
		//				new ItemStack(FABlocks.concrete.ToBlock()), 0.7f);

	}

}
