package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
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
										 "ingot_to_nugget_" + Metals.values()[i]
												 .getName()),
					new ItemStack(FAItems.nugget.ToItem(), 9, i), "I", 'I',
					"ingot" + StringUtils
							.capitalize(Metals.values()[i].getName()));

			ingotToNugget.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 "ingot_to_nugget_" + Metals.values()[i]
												 .getName()));

			IRecipe nuggetToIngot = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 "nugget_to_ingot_" + Metals.values()[i]
												 .getName()),
					new ItemStack(FAItems.ingot.ToItem(), 1, i), "NNN", "NNN",
					"NNN", 'N', "nugget" + StringUtils
					.capitalize(Metals.values()[i].getName()));

			nuggetToIngot.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 "nugget_to_ingot_" + Metals.values()[i]
												 .getName()));

			recipes.add(ingotToNugget);
			recipes.add(nuggetToIngot);
			//TODO aaaAAAA
			AddToolRecipes("bronze", "ingotBronze", "stickWood",
						   FAItems.bronzePickaxe, FAItems.bronzeAxe,
						   FAItems.bronzeSword, FAItems.bronzeHoe,
						   FAItems.bronzeShovel);

		}

		event.getRegistry().registerAll(recipes.toArray(new IRecipe[] {}));

		//
		//
		// ================ BEGIN CUSTOM RECIPE REGISTERING ================
		//
		//

		SteelmakingRecipe.AddRecipe("pigiron-steel-nogases-noflux",
									new SteelmakingRecipe(Arrays.asList(
											Ingredient.fromStacks(new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.PIG_IRON.GetId())),
											Ingredient.fromStacks(new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.PIG_IRON.GetId())),
											Ingredient.fromStacks(new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.PIG_IRON.GetId())),
											Ingredient.fromStacks(new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.PIG_IRON.GetId()))),
														  null, Arrays.asList(
											new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.STEEL.GetId()),
											new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.STEEL.GetId()),
											new ItemStack(
													FAItems.ingot.ToItem(), 1,
													Metals.STEEL.GetId())), 1000,
														  1300));

	}

	private static void AddToolRecipes(String materialName,
			@Nonnull Object ingot, @Nonnull Object stick,
			@Nullable FAItem pickaxe, @Nullable FAItem axe,
			@Nullable FAItem sword, @Nullable FAItem hoe,
			@Nullable FAItem spade)
	{
		if (pickaxe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_pickaxe"),
					new ItemStack(pickaxe.ToItem()), "iii", " s ", " s ", 'i',
					ingot, 's', stick);
			recipes.add(r.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_pickaxe")));
		}
		if (axe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_axe"),
					new ItemStack(axe.ToItem()), "ii", "is", " s", 'i', ingot,
					's', stick);
			recipes.add(r.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_axe")));
		}
		if (hoe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_sword"),
					new ItemStack(hoe.ToItem()), "ii", " s", " s", 'i', ingot,
					's', stick);
			recipes.add(r.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_hoe")));
		}
		if (sword != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_hoe"),
					new ItemStack(sword.ToItem()), "i", "i", "s", 'i', ingot,
					's', stick);
			recipes.add(r.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_sword")));
		}
		if (spade != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_spade"),
					new ItemStack(spade.ToItem()), "i", "s", "s", 'i', ingot,
					's', stick);
			recipes.add(r.setRegistryName(
					new ResourceLocation(FactoryAutomation.MODID,
										 materialName + "_spade")));
		}

	}

}
