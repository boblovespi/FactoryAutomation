package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.api.recipe.BasicCircuitRecipe;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@Mod.EventBusSubscriber

public class RecipeHandler
{
	public static List<IRecipe> recipes;
	private static IRecipe concrete;

	private static NonNullList<ItemStack> bronzeCrucibleItems = NonNullList
			.from(ItemStack.EMPTY, new ItemStack(FAItems.nugget.GetItem(Metals.COPPER), 7),
					new ItemStack(FAItems.nugget.GetItem(Metals.TIN), 1));
	private static NonNullList<ItemStack> bronze = NonNullList
			.from(ItemStack.EMPTY, new ItemStack(FAItems.nugget.GetItem(Metals.BRONZE), 8));

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerIRecipes(RegistryEvent.Register<IRecipe> event)
	{
		//		concrete = new ShapelessOreRecipe(new ResourceLocation(FactoryAutomation.MODID, "concrete"),
		//				FABlocks.concrete.ToBlock(), FAItems.slag.ToItem(), Items.WATER_BUCKET, Blocks.SAND)
		//				.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "concrete"));

		recipes = new ArrayList<>();
		// recipes.add(concrete);

		for (int i = 2; i < Metals.values().length; i++)
		{
			IRecipe ingotToNugget = new ShapedOreRecipe(
					new ResourceLocation(MODID, "ingot_to_nugget_" + Metals.values()[i].getName()),
					new ItemStack(FAItems.nugget.GetItem(Metals.values()[i]), 9), "I", 'I',
					"ingot" + StringUtils.capitalize(Metals.values()[i].getName()));

			ingotToNugget.setRegistryName(
					new ResourceLocation(MODID, "ingot_to_nugget_" + Metals.values()[i].getName()));

			IRecipe nuggetToIngot = new ShapedOreRecipe(
					new ResourceLocation(MODID, "nugget_to_ingot_" + Metals.values()[i].getName()),
					new ItemStack(FAItems.ingot.GetItem(Metals.values()[i]), 1), "NNN", "NNN", "NNN", 'N',
					"nugget" + StringUtils.capitalize(Metals.values()[i].getName()));

			nuggetToIngot.setRegistryName(
					new ResourceLocation(MODID, "nugget_to_ingot_" + Metals.values()[i].getName()));

			recipes.add(ingotToNugget);
			recipes.add(nuggetToIngot);

			AddToolRecipes("bronze", "ingotBronze", "stickWood", FAItems.bronzePickaxe, FAItems.bronzeAxe,
					FAItems.bronzeSword, FAItems.bronzeHoe, FAItems.bronzeShovel);

			AddToolRecipes("steel", "ingotSteel", "stickWood", FAItems.steelPickaxe, FAItems.steelAxe,
					FAItems.steelSword, FAItems.steelHoe, FAItems.steelShovel);

			AddToolRecipes("copper", "ingotCopper", "stickWood", FAItems.copperPickaxe, FAItems.copperAxe,
					FAItems.copperSword, FAItems.copperHoe, FAItems.copperShovel);

		}

		ItemStack filledCrucibleStack = new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0);
		NBTTagCompound filledTag = new NBTTagCompound();
		filledTag.setTag("items", new ItemStackHandler(bronzeCrucibleItems).serializeNBT());
		filledCrucibleStack.setTagCompound(filledTag);

		ItemStack bronzeCrucibleStack = new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0);
		NBTTagCompound filledTag2 = new NBTTagCompound();
		filledTag2.setTag("items", new ItemStackHandler(bronze).serializeNBT());
		bronzeCrucibleStack.setTagCompound(filledTag2);

		IRecipe filledCrucible = new ShapelessOreRecipe(
				new ResourceLocation(MODID, "filled_bronze_crucible"), filledCrucibleStack,
				new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0), "nuggetCopper", "nuggetCopper", "nuggetCopper",
				"nuggetCopper", "nuggetCopper", "nuggetCopper", "nuggetCopper", "nuggetTin");
		filledCrucible.setRegistryName(new ResourceLocation(MODID, "filled_bronze_crucible"));
		recipes.add(filledCrucible);

		event.getRegistry().registerAll(recipes.toArray(new IRecipe[] {}));

		//
		//
		// ========= FURNACE RECIPES =========
		//
		//

		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.COPPER), 1),
				new ItemStack(FAItems.ingot.GetItem(Metals.COPPER), 1), 0.7f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.TIN), 1),
				new ItemStack(FAItems.ingot.GetItem(Metals.TIN), 1), 0.7f);

		FurnaceRecipes.instance().addSmeltingRecipe(filledCrucibleStack, bronzeCrucibleStack, 10f);

		//
		//
		// ================ BEGIN CUSTOM RECIPE REGISTERING ================
		//
		//

		SteelmakingRecipe.AddRecipe(
				"pigiron-steel-nogases-noflux", new SteelmakingRecipe(
						Arrays.asList(Ingredient.fromStacks(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.fromStacks(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.fromStacks(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.fromStacks(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON)))), null,
						Arrays.asList(new ItemStack(FAItems.ingot.GetItem(Metals.STEEL)),
								new ItemStack(FAItems.ingot.GetItem(Metals.STEEL)),
								new ItemStack(FAItems.ingot.GetItem(Metals.STEEL))), 1000, 1300));

		//

		//

		JawCrusherRecipe.AddRecipe(new JawCrusherRecipe(Ingredient.fromItem(Item.getItemFromBlock(Blocks.STONE)),
				n -> new ItemStack(Blocks.COBBLESTONE), 0, "stone-to-cobblestone", 20, 10, 10));

		JawCrusherRecipe.AddRecipe(new JawCrusherRecipe(Ingredient.fromItem(Item.getItemFromBlock(Blocks.DIAMOND_ORE)),
				n -> new ItemStack(FAItems.diamondGravel.ToItem()), 0, "diamond-processing", 20, 10, 100));

		//

		//

		WorkbenchRecipeHandler.LoadFromJson(Loader.instance().activeModContainer(),
				new ResourceLocation(MODID, "workbench_recipes"));

		BasicCircuitRecipe.LoadFromJson(Loader.instance().activeModContainer(), new ResourceLocation(MODID, "basic_circuit_recipes"));

	}

	private static void AddToolRecipes(String materialName, @Nonnull Object ingot, @Nonnull Object stick,
			@Nullable FAItem pickaxe, @Nullable FAItem axe, @Nullable FAItem sword, @Nullable FAItem hoe,
			@Nullable FAItem spade)
	{
		if (pickaxe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(MODID, materialName + "_pickaxe"),
					new ItemStack(pickaxe.ToItem()), "iii", " s ", " s ", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_pickaxe")));
		}
		if (axe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(MODID, materialName + "_axe"), new ItemStack(axe.ToItem()),
					"ii", "is", " s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_axe")));
		}
		if (hoe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(MODID, materialName + "_sword"), new ItemStack(hoe.ToItem()),
					"ii", " s", " s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_hoe")));
		}
		if (sword != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(MODID, materialName + "_hoe"), new ItemStack(sword.ToItem()),
					"i", "i", "s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_sword")));
		}
		if (spade != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(
					new ResourceLocation(MODID, materialName + "_spade"),
					new ItemStack(spade.ToItem()), "i", "s", "s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_spade")));
		}

	}

}
