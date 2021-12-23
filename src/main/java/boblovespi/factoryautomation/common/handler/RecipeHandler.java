package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.api.recipe.*;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.item.types.WoodTypes;
import boblovespi.factoryautomation.common.util.recipes.AxeRecipe;
import boblovespi.factoryautomation.common.util.recipes.HammerRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.item.ores.OreForms.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeHandler
{
	public static List<Recipe<?>> recipes;

	//	private static NonNullList<ItemStack> bronzeCrucibleItems = NonNullList
	//			.from(ItemStack.EMPTY, new ItemStack(FAItems.nugget.GetItem(Metals.COPPER), 7),
	//					new ItemStack(FAItems.nugget.GetItem(Metals.TIN), 1));
	//	private static NonNullList<ItemStack> bronze = NonNullList
	//			.from(ItemStack.EMPTY, new ItemStack(FAItems.nugget.GetItem(Metals.BRONZE), 8));

	public static void registerIRecipes()
	{
		//		concrete = new ShapelessOreRecipe(new ResourceLocation(FactoryAutomation.MODID, "concrete"),
		//				FABlocks.concrete.ToBlock(), FAItems.slag.ToItem(), Items.WATER_BUCKET, Blocks.SAND)
		//				.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "concrete"));
		/*
		recipes = new ArrayList<>();
		// recipes.add(concrete);

		// recipe removal
		if (event.getRegistry() instanceof ForgeRegistry)
		{
			ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) event.getRegistry();
			registry.remove(new ResourceLocation("minecraft", "oak_planks"));
			registry.remove(new ResourceLocation("minecraft", "spruce_planks"));
			registry.remove(new ResourceLocation("minecraft", "acacia_planks"));
			registry.remove(new ResourceLocation("minecraft", "jungle_planks"));
			registry.remove(new ResourceLocation("minecraft", "dark_oak_planks"));
			registry.remove(new ResourceLocation("minecraft", "birch_planks"));
			registry.remove(new ResourceLocation("minecraft", "stick"));
			registry.remove(new ResourceLocation("minecraft", "wooden_pickaxe"));
			registry.remove(new ResourceLocation("minecraft", "furnace"));
		}
		AddToolRecipes("bronze", "ingotBronze", "stickWood", FAItems.bronzePickaxe, FAItems.bronzeAxe,
				FAItems.bronzeSword, FAItems.bronzeHoe, FAItems.bronzeShovel);

		AddToolRecipes("steel", "ingotSteel", "stickWood", FAItems.steelPickaxe, FAItems.steelAxe, FAItems.steelSword,
				FAItems.steelHoe, FAItems.steelShovel);

		AddToolRecipes("copper", "ingotCopper", "stickWood", FAItems.copperPickaxe, FAItems.copperAxe,
				FAItems.copperSword, FAItems.copperHoe, FAItems.copperShovel);

		AddPlankAndStickRecipes((ItemAxe) Items.IRON_AXE, "iron_axe");
		AddPlankAndStickRecipes((ItemAxe) Items.DIAMOND_AXE, "diamond_axe");
		AddPlankAndStickRecipes((ItemAxe) FAItems.steelAxe, "steel_axe");
		AddPlankAndStickRecipes((ItemAxe) FAItems.bronzeAxe, "bronze_axe");

		ItemStack filledCrucibleStack = new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0);
		CompoundNBT filledTag = new CompoundNBT();
		filledTag.setTag("items", new ItemStackHandler(bronzeCrucibleItems).serializeNBT());
		filledCrucibleStack.setTagCompound(filledTag);

		ItemStack bronzeCrucibleStack = new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0);
		CompoundNBT filledTag2 = new CompoundNBT();
		filledTag2.setTag("items", new ItemStackHandler(bronze).serializeNBT());
		bronzeCrucibleStack.setTagCompound(filledTag2);

		IRecipe filledCrucible = new ShapelessOreRecipe(new ResourceLocation(MODID, "filled_bronze_crucible"),
				filledCrucibleStack, new ItemStack(FAItems.clayCrucible.ToItem(), 1, 0), "nuggetCopper", "nuggetCopper",
				"nuggetCopper", "nuggetCopper", "nuggetCopper", "nuggetCopper", "nuggetCopper", "nuggetTin");
		filledCrucible.setRegistryName(new ResourceLocation(MODID, "filled_bronze_crucible"));
		recipes.add(filledCrucible);

		HammerRecipe rec = new HammerRecipe(new ResourceLocation(FactoryAutomation.MODID, "acid_powder"),
				new ItemStack(FAItems.acidPowder.ToItem(), 8), "dgd", "ghg", "drd", 'd', "glycerin", 'g', "gunpowder",
				'r', "gemGraphite", 'h', Ingredient
				.of(new ItemStack(FAItems.ironHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(FAItems.steelHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE)));
		rec.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "acid_powder"));
		recipes.add(rec);

		for (Metals metal : Metals.values())
		{
			HammerRecipe rec1 = new HammerRecipe(
					new ResourceLocation(FactoryAutomation.MODID, "nugget_to_coin_" + metal.getName()),
					new ItemStack(FAItems.coin.GetItem(metal), 1), "nh", 'n', "nugget" + Cleanup(metal.getName()), 'h',
					Ingredient.of(new ItemStack(FAItems.ironHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE),
							new ItemStack(FAItems.steelHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE)));
			rec1.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "nugget_to_coin_" + metal.getName()));
			recipes.add(rec1);
			HammerRecipe rec2 = new HammerRecipe(
					new ResourceLocation(FactoryAutomation.MODID, "coin_to_nugget_" + metal.getName()),
					OreDictionary.getOres("nugget" + Cleanup(metal.getName())).stream()
								 .filter(n -> n.getItem() instanceof FAItem || n.getItem().getRegistryName()
																				.getResourceDomain()
																				.contains("minecraft") || n.getItem()
																										   .getRegistryName()
																										   .getResourceDomain()
																										   .equals("factoryautomation"))
								 .findFirst().orElse(ItemStack.EMPTY)

					, "nh", 'n', FAItems.coin.GetItem(metal), 'h', Ingredient
					.of(new ItemStack(FAItems.ironHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE),
							new ItemStack(FAItems.steelHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE)));
			rec2.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "coin_to_nugget_" + metal.getName()));
			recipes.add(rec2);
		}
		HammerRecipe rec1 = new HammerRecipe(new ResourceLocation(FactoryAutomation.MODID, "nugget_to_coin_diamond"),
				new ItemStack(FAItems.diamondCoin.ToItem(), 1), "nh", 'n', "gemDiamond", 'h', Ingredient
				.of(new ItemStack(FAItems.ironHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(FAItems.steelHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE)));
		rec1.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "nugget_to_coin_diamond"));
		recipes.add(rec1);
		HammerRecipe rec2 = new HammerRecipe(new ResourceLocation(FactoryAutomation.MODID, "coin_to_nugget_diamond"),
				new ItemStack(Items.DIAMOND), "nh", 'n', FAItems.diamondCoin.ToItem(), 'h', Ingredient
				.of(new ItemStack(FAItems.ironHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(FAItems.steelHammer.ToItem(), 1, OreDictionary.WILDCARD_VALUE)));
		rec2.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, "coin_to_nugget_diamond"));
		recipes.add(rec2);

		event.getRegistry().registerAll(recipes.toArray(new IRecipe[] {}));

		//
		//
		// ========= FURNACE RECIPES =========
		//
		//

		//		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.COPPER), 1),
		//				new ItemStack(FAItems.ingot.GetItem(Metals.COPPER), 1), 0.7f);
		//		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.TIN), 1),
		//				new ItemStack(FAItems.ingot.GetItem(Metals.TIN), 1), 0.7f);

		FurnaceRecipes.instance().addSmeltingRecipe(filledCrucibleStack, bronzeCrucibleStack, 10f);

		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FAItems.liquidGlycerin.ToItem()),
				new ItemStack(FAItems.dryGlycerin.ToItem()), 0.4f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FAItems.rawRubber.ToItem()),
				new ItemStack(FAItems.rubber.ToItem()), 0.6f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(FAItems.slag.ToItem()),
				new ItemStack(FABlocks.slagGlass.ToBlock()), 0.6f);
		*/
		//
		//
		// ================ BEGIN CUSTOM RECIPE REGISTERING ================
		//
		//

		SteelmakingRecipe.AddRecipe(
				"pigiron-steel-nogases-noflux", new SteelmakingRecipe(
						Arrays.asList(Ingredient.of(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.of(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.of(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON))),
								Ingredient.of(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON)))), null,
						Arrays.asList(new ItemStack(FAItems.ingot.GetItem(Metals.STEEL)),
								new ItemStack(FAItems.ingot.GetItem(Metals.STEEL)),
								new ItemStack(FAItems.ingot.GetItem(Metals.STEEL))), 1000, 1300));

		//
		// jaw crusher
		//

		JawCrusherRecipe.AddRecipe(
				new JawCrusherRecipe(Ingredient.of(Blocks.STONE), n -> new ItemStack(Blocks.COBBLESTONE), 0,
						"stone-to-cobblestone", 20, 10, 10));

		JawCrusherRecipe.AddRecipe(new JawCrusherRecipe(Ingredient.of(Blocks.DIAMOND_ORE),
				n -> new ItemStack(FAItems.diamondGravel.ToItem()), 0, "diamond-processing", 20, 10, 100));

		JawCrusherRecipe.AddRecipe(
				new JawCrusherRecipe(Ingredient.of(FAItems.processedMagnetite.GetItem(CHUNK)),
						new HashMap<Float, ItemStack>()
						{{
							put(0.8f, new ItemStack(FAItems.processedMagnetite.GetItem(NORMAL_GRAVEL)));
							put(0.12f, new ItemStack(FAItems.processedMagnetite.GetItem(NORMAL_FINE_GRAVEL)));
							put(0.08f, new ItemStack(FAItems.processedMagnetite.GetItem(CHUNK)));
						}}, 0, "magnetite-normal-coarse-to-gravel", 20, 10, 100));

		// trip hammer recipes

		new TripHammerRecipe("iron-block-to-sheets", Ingredient.of(Items.IRON_BLOCK),
				new ItemStack(FAItems.sheet.GetItem(Metals.IRON), 6), 100, 10);

		// chopping block recipes
		for (WoodTypes type : WoodTypes.values())
		{
			ChoppingBlockRecipe.AddRecipe(type.GetName() + "_log_to_plank", type.GetLog().asItem(),
					new ItemStack(type.GetPlanks(), 4));
		}

		// ChoppingBlockRecipe.AddRecipe("plank_to_stick", new ResourceLocation("minecraft:planks"), new ItemStack(Items.STICK, 4));
		ChoppingBlockRecipe
				.AddRecipe("grass_to_fiber", Blocks.GRASS.asItem(), new ItemStack(FAItems.plantFiber.ToItem(), 2));

		// campfire recipes
		CampfireRecipe.AddRecipe("cooked_pork", Items.PORKCHOP, new ItemStack(Items.COOKED_PORKCHOP), 20 * 60 * 4);
		CampfireRecipe.AddRecipe("steak", Items.BEEF, new ItemStack(Items.COOKED_BEEF), 20 * 60 * 2);
		CampfireRecipe.AddRecipe("cooked_rabbit", Items.RABBIT, new ItemStack(Items.COOKED_RABBIT), 20 * 60 * 5);
		CampfireRecipe.AddRecipe("cooked_chicken", Items.CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 20 * 60 * 5);
		CampfireRecipe.AddRecipe("mutton", Items.MUTTON, new ItemStack(Items.COOKED_MUTTON), 20 * 60 * 3);
		CampfireRecipe.AddRecipe("chorus_popcorn", Items.CHORUS_FRUIT, new ItemStack(Items.POPPED_CHORUS_FRUIT), 600);
		CampfireRecipe.AddRecipe("baked_potato", Items.POTATO, new ItemStack(Items.BAKED_POTATO), 600);
		CampfireRecipe.AddRecipe("toasted_bread", Items.BREAD, new ItemStack(FAItems.toastedBread.ToItem()), 300);
		//CampfireRecipe.AddRecipe("flatbread", Items.CHORUS_FRUIT, 0, new ItemStack(Items.CHORUS_FRUIT_POPPED),
		//		20 * 60 * 3); // TODO: add basic bread
		// fish
		CampfireRecipe.AddRecipe("salmon", Items.SALMON, new ItemStack(Items.COOKED_SALMON, 1), 20 * 60 * 3);
		CampfireRecipe.AddRecipe("cod", Items.COD, new ItemStack(Items.COOKED_COD, 1), 20 * 60 * 3);

		// millstone recipes
		MillstoneRecipe.AddRecipe("wheat_flour", Items.WHEAT, 50, 1, new ItemStack(FAItems.wheatFlour.ToItem()));
		MillstoneRecipe.AddRecipe("bone_meal", Items.BONE, 30, 5, new ItemStack(Items.BONE_MEAL, 4));

		//

		//

		/*
		WorkbenchRecipeHandler
				.LoadFromJson(Loader.instance().activeModContainer(), new ResourceLocation(MODID, "recipes"));

		BasicCircuitRecipe.LoadFromJson(Loader.instance().activeModContainer(), new ResourceLocation(MODID, "recipes"));

		recipes = null; // clear the cache
		*/

	}

	/*
	private static void AddToolRecipes(String materialName, @Nonnull Object ingot, @Nonnull Object stick,
			@Nullable FAItem pickaxe, @Nullable FAItem axe, @Nullable FAItem sword, @Nullable FAItem hoe,
			@Nullable FAItem spade)
	{
		if (pickaxe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(new ResourceLocation(MODID, materialName + "_pickaxe"),
					new ItemStack(pickaxe.ToItem()), "iii", " s ", " s ", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_pickaxe")));
		}
		if (axe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(new ResourceLocation(MODID, materialName + "_axe"),
					new ItemStack(axe.ToItem()), "ii", "is", " s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_axe")));
		}
		if (hoe != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(new ResourceLocation(MODID, materialName + "_sword"),
					new ItemStack(hoe.ToItem()), "ii", " s", " s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_hoe")));
		}
		if (sword != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(new ResourceLocation(MODID, materialName + "_hoe"),
					new ItemStack(sword.ToItem()), "i", "i", "s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_sword")));
		}
		if (spade != null)
		{
			ShapedOreRecipe r = new ShapedOreRecipe(new ResourceLocation(MODID, materialName + "_spade"),
					new ItemStack(spade.ToItem()), "i", "s", "s", 'i', ingot, 's', stick);
			recipes.add(r.setRegistryName(new ResourceLocation(MODID, materialName + "_spade")));
		}
	}

	private static void AddPlankAndStickRecipes(ItemAxe axe, String axeName)
	{
		for (BlockPlanks.EnumType woodType : BlockPlanks.EnumType.values())
		{
			AxeRecipe planks;

			if (woodType.getMetadata() < 4)
				planks = new AxeRecipe(new ResourceLocation(MODID, woodType.getName() + "_planks_via_" + axeName),
						new ItemStack(Blocks.PLANKS, 4, woodType.getMetadata()), "la", 'l',
						new ItemStack(Blocks.LOG, 1, woodType.getMetadata()), 'a', axe);
			else
				planks = new AxeRecipe(new ResourceLocation(MODID, woodType.getName() + "_planks_via_" + axeName),
						new ItemStack(Blocks.PLANKS, 4, woodType.getMetadata()), "la", 'l',
						new ItemStack(Blocks.LOG2, 1, woodType.getMetadata()), 'a', axe);
			planks.setRegistryName(new ResourceLocation(MODID, woodType.getName() + "_planks_via_" + axeName));
			recipes.add(planks);
		}
		AxeRecipe sticks = new AxeRecipe(new ResourceLocation(MODID, "sticks_via_" + axeName),
				new ItemStack(Items.STICK, 2), "pa", 'p', "plankWood", 'a', axe);
		sticks.setRegistryName(new ResourceLocation(MODID, "sticks_via_" + axeName));

		recipes.add(sticks);
	}

	public static void RemoveSmeltingRecipes()
	{
		// furnace recipe removal
		List<Ingredient> removeList = new ArrayList<>();
		removeList.add(new OreIngredient("oreIron"));
		removeList.add(new OreIngredient("oreGold"));
		removeList.add(new OreIngredient("oreCopper"));
		removeList.add(new OreIngredient("oreDiamond"));
		removeList.add(new OreIngredient("oreTin"));
		removeList.add(Ingredient.of(Items.CLAY_BALL));
		removeList.add(Ingredient.of(Item.getItemFromBlock(Blocks.GOLD_ORE)));
		removeList.add(Ingredient.of(Item.getItemFromBlock(Blocks.IRON_ORE)));
		removeList.add(Ingredient.of(Item.getItemFromBlock(Blocks.DIAMOND_ORE)));
		List<ItemStack> toRemoveList = new ArrayList<>();

		for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet())
		{
			for (Ingredient ingredient : removeList)
			{
				if (ingredient.apply(entry.getKey()))
				{
					toRemoveList.add(entry.getKey());
					break;
				}
			}
		}

		for (ItemStack stack : toRemoveList)
		{
			FurnaceRecipes.instance().getSmeltingList().remove(stack);
		}
	} */

	@SubscribeEvent
	public static void RegisterSerializers(RegistryEvent.Register<RecipeSerializer<?>> event)
	{
		BasicCircuitRecipe.SERIALIZER.setRegistryName(MODID, "basic_circuit");
		event.getRegistry().register(BasicCircuitRecipe.SERIALIZER);
		WorkbenchRecipeHandler.SHAPED_SERIALIZER.setRegistryName(MODID, "workbench_shaped");
		event.getRegistry().register(WorkbenchRecipeHandler.SHAPED_SERIALIZER);
		HammerRecipe.SERIALIZER.setRegistryName(MODID, "hammer_recipe");
		event.getRegistry().register(HammerRecipe.SERIALIZER);
		AxeRecipe.SERIALIZER.setRegistryName(MODID, "axe_recipe");
		event.getRegistry().register(AxeRecipe.SERIALIZER);
		ChoppingBlockRecipe.SERIALIZER.setRegistryName(MODID, "chopping_block");
		event.getRegistry().register(ChoppingBlockRecipe.SERIALIZER);
	}
}
