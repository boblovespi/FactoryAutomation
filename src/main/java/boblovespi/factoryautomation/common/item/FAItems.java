package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.handler.OreDictionaryHandler;
import boblovespi.factoryautomation.common.item.crucible.ClayCrucible;
import boblovespi.factoryautomation.common.item.metals.Ingot;
import boblovespi.factoryautomation.common.item.metals.MetalItem;
import boblovespi.factoryautomation.common.item.metals.Nugget;
import boblovespi.factoryautomation.common.item.metals.Sheet;
import boblovespi.factoryautomation.common.item.ores.OreForms;
import boblovespi.factoryautomation.common.item.ores.ProcessedOre;
import boblovespi.factoryautomation.common.item.tools.*;
import boblovespi.factoryautomation.common.item.types.IceCreams;
import boblovespi.factoryautomation.common.item.types.MachineTiers;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.item.types.TallowForms;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import boblovespi.factoryautomation.common.util.SoundHandler;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static boblovespi.factoryautomation.common.item.tools.ToolMaterial.*;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FAItems
{
	private static final AtomicBoolean isInit = new AtomicBoolean(false);
	public static final List<RegistryObjectWrapper<Item>> items = new ArrayList<>(100);

	// metal resources

	public static MultiTypeItem<Metals> ingot;
	public static MultiTypeItem<Metals> nugget;
	public static MultiTypeItem<Metals> sheet;
	public static MultiTypeItem<Metals> coin;
	public static MultiTypeItem<Metals> rod;
	public static Item diamondCoin;
	public static Item voidsteelIngot;

	// metallurgy misc

	public static Item slag;
	public static Item coalCoke;
	public static Item ironShard;
	public static MultiTypeItem<TallowForms> pigTallowParts;
	public static MultiTypeItem<TallowForms> pigTallowMolds;
	public static MultiTypeItem<TallowForms> firedMolds;

	// ore processing forms

	public static MultiTypeItem<OreForms> processedMagnetite;
	public static MultiTypeItem<OreForms> processedLimonite;
	public static MultiTypeItem<OreForms> processedCassiterite;

	// food

	public static Item riceGrain;
	public static Item toastedBread;
	public static Item wheatFlour;
	public static Item[] iceCream;
	public static Item pancake;
	public static Item honeyPancake;

	// resources

	public static Item diamondGravel;
	public static Item stoneDust;
	public static Item porcelainClay;
	public static Item siliconQuartz;
	public static Item ash;
	public static Item liquidGlycerin;
	public static Item dryGlycerin;
	public static Item acidPowder;
	public static Item rawRubber;
	public static Item rubber;
	public static Item graphite;
	public static Item terraclay;
	public static Item terraclayBrick;
	public static Item plantFiber;
	public static Item pigTallow;
	public static Item tanbarkDust;
	public static Item calciteDust;
	public static Item quicklime;
	public static Item cleanedLeather;
	public static Item processedLeather;

	// crafting components

	public static MultiTypeItem<Gearbox.GearType> gear;
	public static Item glassLens;
	public static Item airPiston;
	public static Item bronzeFlywheel;
	public static Item stirlingGeneratorCore;

	// crafting parts

	public static Item screw;

	// electrical parts

	public static Item copperWire;
	public static Item basicChip;
	public static Item circuitFrame;
	public static Item dataprintCircuit;

	// regular tools

	public static Item bronzePickaxe;
	public static Item bronzeAxe;
	public static Item bronzeHoe;
	public static Item bronzeShovel;
	public static Item bronzeSword;

	public static Item steelPickaxe;
	public static Item steelAxe;
	public static Item steelHoe;
	public static Item steelShovel;
	public static Item steelSword;

	public static Item copperPickaxe;
	public static Item copperAxe;
	public static Item copperHoe;
	public static Item copperShovel;
	public static Item copperSword;
	public static Item copperShears;

	public static Item voidsteelPickaxe;
	public static Item voidsteelAxe;
	public static Item voidsteelHoe;
	public static Item voidsteelShovel;
	public static Item voidsteelSword;

	public static Item flintPickaxe;

	public static Item choppingBlade;
	public static Item firebow;

	// workbench tools

	public static Item copperHammer;
	public static Item ironHammer;
	public static Item steelHammer;
	public static Item ironWrench;
	public static Item bronzeWrench;
	public static Item steelWrench;
	public static Item steelPinchers;
	public static Item sandpaper;
	public static Item advancedFlintAndSteel;

	// misc tools

	public static Item clayCrucible;
	public static MultiTypeItem<MachineTiers> wearPlate;

	// fluid canister

	public static Item fluidCanister;

	// misc

	public static Item factoryDisc;
	public static Item meterDisc;

	// guidebook

	// public static FAItem guidebook;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		// metal resources

		ingot = new Ingot();
		RemoveItem(ingot.GetItem(Metals.IRON));
		RemoveItem(ingot.GetItem(Metals.GOLD));
		RemoveItem(ingot.GetItem(Metals.COPPER));
		nugget = new Nugget();
		RemoveItem(nugget.GetItem(Metals.IRON));
		RemoveItem(nugget.GetItem(Metals.GOLD));
		sheet = new Sheet();
		coin = new MetalItem("coin");
		diamondCoin = new FABaseItem("coin_diamond", CreativeModeTab.TAB_MISC);
		rod = new MetalItem("rod");

		// metallurgy misc

		slag = new FABaseItem("slag", FAItemGroups.metallurgy);
		coalCoke = new FAFuel("coal_coke", FAItemGroups.metallurgy, 2000);
		ironShard = new FABaseItem("iron_shard", FAItemGroups.metallurgy);
		pigTallowParts = new TallowPart();
		pigTallowMolds = new MultiTypeItem<>(
				"tallow_mold", TallowForms.class, "molds", Prop().tab(FAItemGroups.metallurgy));
		firedMolds = new MultiTypeItem<>(
				"fired_mold", TallowForms.class, "molds", Prop().tab(FAItemGroups.metallurgy));

		// ore processing forms

		processedMagnetite = new ProcessedOre("magnetite");
		processedLimonite = new ProcessedOre("limonite");
		processedCassiterite = new ProcessedOre("cassiterite");

		// foods

		riceGrain = new RiceGrain();
		toastedBread = new FAFood(
				"toasted_bread", 5, 4, 32, false, false, Collections.emptyList(), Collections.emptyList());
		wheatFlour = new FABaseItem("wheat_flour", FAItemGroups.resources);
		iceCream = new Item[IceCreams.values().length];
		for (int i = 0; i < iceCream.length; i++)
		{
			iceCream[i] = new FAFood("ice_cream_" + IceCreams.values()[i].name().toLowerCase(), 8, 12, 32, false, true,
					IceCreams.values()[i].GetPotionEffects(), Collections.singletonList(1f));
		}
		pancake = new FABaseItem("pancake", Prop().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.8f).build()));
		honeyPancake = new FABaseItem("honey_pancake", Prop().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(11).saturationMod(1f).build()));

		// resources

		diamondGravel = new CauldronCleanable("diamond_gravel", FAItemGroups.resources, new ItemStack(Items.DIAMOND));
		stoneDust = new FABaseItem("stone_dust", FAItemGroups.resources);
		porcelainClay = new FABaseItem("porcelain_clay", FAItemGroups.resources);
		siliconQuartz = new FABaseItem("silicon_quartz_crystal", FAItemGroups.resources);
		ash = new FABaseItem("ash", FAItemGroups.resources);
		liquidGlycerin = new FABaseItem("liquid_glycerin", FAItemGroups.resources);
		dryGlycerin = new FABaseItem("dry_glycerin", FAItemGroups.resources);
		acidPowder = new FABaseItem("acid_powder", FAItemGroups.resources);
		rawRubber = new FABaseItem("raw_rubber", FAItemGroups.resources);
		rubber = new FABaseItem("rubber", FAItemGroups.resources);
		graphite = new FABaseItem("graphite", FAItemGroups.resources);
		terraclay = new FABaseItem("terraclay", FAItemGroups.resources);
		terraclayBrick = new FABaseItem("terraclay_brick", FAItemGroups.resources);
		plantFiber = new FABaseItem("plant_fiber", FAItemGroups.resources);
		pigTallow = new FABaseItem("pig_tallow", FAItemGroups.resources);
		tanbarkDust = new FABaseItem("tanbark_dust", FAItemGroups.resources);
		calciteDust = new FABaseItem("calcite_dust", FAItemGroups.resources);
		quicklime = new FABaseItem("quicklime", FAItemGroups.resources);
		cleanedLeather = new FABaseItem("cleaned_leather", FAItemGroups.resources);
		processedLeather = new FABaseItem("processed_leather", FAItemGroups.resources);
		voidsteelIngot = new FABaseItem("ingot_voidsteel", FAItemGroups.metallurgy);

		// crafting components

		gear = new Gear();
		glassLens = new FABaseItem("glass_lens", FAItemGroups.crafting);
		airPiston = new FABaseItem("air_piston", FAItemGroups.crafting);
		bronzeFlywheel = new FABaseItem("bronze_flywheel", FAItemGroups.crafting);
		stirlingGeneratorCore = new FABaseItem("stirling_generator_core", FAItemGroups.crafting);

		// crafting parts

		screw = new WorkbenchPartItem("screw", FAItemGroups.crafting);

		// electrical parts

		copperWire = new FABaseItem("copper_wire", FAItemGroups.electrical);
		basicChip = new FABaseItem("basic_chip", FAItemGroups.electrical);
		circuitFrame = new FABaseItem("circuit_frame", FAItemGroups.electrical);
		dataprintCircuit = new FABaseItem("dataprint_circuit", FAItemGroups.electrical);

		// regular tools

		bronzePickaxe = new FAPickaxe(bronzeMaterial, "bronze_pickaxe");
		bronzeAxe = new FAAxe(bronzeMaterial, "bronze_axe");
		bronzeHoe = new FAHoe(bronzeMaterial, "bronze_hoe", -1.5f);
		bronzeShovel = new FAShovel(bronzeMaterial, "bronze_shovel");
		bronzeSword = new FASword(bronzeMaterial, "bronze_sword");

		steelAxe = new FAAxe(steelMaterial, "steel_axe");
		steelHoe = new FAHoe(steelMaterial, "steel_hoe", -0.5f);
		steelShovel = new FAShovel(steelMaterial, "steel_shovel");
		steelSword = new FASword(steelMaterial, "steel_sword");
		steelPickaxe = new FAPickaxe(steelMaterial, "steel_pickaxe");

		copperAxe = new FAAxe(copperMaterial, "copper_axe");
		copperHoe = new FAHoe(copperMaterial, "copper_hoe", -2.5f);
		copperShovel = new FAShovel(copperMaterial, "copper_shovel");
		copperSword = new FASword(copperMaterial, "copper_sword");
		copperPickaxe = new FAPickaxe(copperMaterial, "copper_pickaxe");
		copperShears = new FAShears("copper_shears", Prop().tab(CreativeModeTab.TAB_TOOLS).durability(176));

		voidsteelAxe = new FAAxe(voidsteelMaterial, "voidsteel_axe");
		voidsteelHoe = new FAHoe(voidsteelMaterial, "voidsteel_hoe", 0f);
		voidsteelShovel = new FAShovel(voidsteelMaterial, "voidsteel_shovel");
		voidsteelPickaxe = new FAPickaxe(voidsteelMaterial, "voidsteel_pickaxe");
		voidsteelSword = new FASword(voidsteelMaterial, "voidsteel_sword");

		flintPickaxe = new FAPickaxe(Tiers.WOOD, "flint_pickaxe");

		choppingBlade = new FAAxe(flintMaterial, "chopping_blade");
		firebow = new Firebow();

		// workbench tools

		copperHammer = new Hammer("copper_hammer", 5, -3.7f, ToolMaterial.copperMaterial);
		ironHammer = new Hammer("iron_hammer", 8, -3.7f, Tiers.IRON);
		steelHammer = new Hammer("steel_hammer", 12, -3.7f, ToolMaterial.steelMaterial);
		ironWrench = new Wrench("iron_wrench", 2f, -2.9f, Tiers.IRON);
		bronzeWrench = new Wrench("bronze_wrench", 2f, -2.9f, bronzeMaterial);
		steelWrench = new Wrench("steel_wrench", 2f, -2.9f, ToolMaterial.steelMaterial);
		steelPinchers = new WorkbenchToolItem(
				"steel_pinchers", 0, 0, ToolMaterial.steelMaterial, FATags.NOTHING_TOOL, Prop());
		sandpaper = new WorkbenchToolItem("sandpaper", 0, 0, Tiers.WOOD,FATags.NOTHING_TOOL, Prop());
		advancedFlintAndSteel = new AdvancedFlintAndSteel();

		// misc tools

		wearPlate = new MultiTypeItem<>("wear_plate", MachineTiers.class, "", Prop());
		clayCrucible = new ClayCrucible();

		// fluidCanister = new FluidCanister("fluid_canister", 3000);

		// guidebook

		// guidebook = new Guidebook();

		// misc

		factoryDisc = new MusicDisc("disc_factory", 15, () -> SoundHandler.factoryDisc.obj(),
				Prop().tab(CreativeModeTab.TAB_MISC), 1 * 60 * 20 + 3 * 20);
		meterDisc = new MusicDisc("disc_meter", 15, () -> SoundHandler.meterDisc.obj(),
				Prop().tab(CreativeModeTab.TAB_MISC), 2 * 60 * 20 + 53 * 20);
	}

	public static Item.Properties Prop()
	{
		return new Item.Properties();
	}

	public static Item.Properties Building()
	{
		return Prop().tab(CreativeModeTab.TAB_BUILDING_BLOCKS);
	}

	public static void RemoveItem(Item item)
	{
		items.removeIf(i -> i.obj() == item);
	}

	public static void RegisterItemRenders()
	{
		/*for (Item item : items)
		{
			Log.LogInfo("new item!", Objects.requireNonNull(item.getRegistryName()));
			// Log.LogInfo("Item unlocalized name", item.getUnlocalizedName());
			// Log.LogInfo("item resource path", item.getRegistryName().getResourcePath());
			if (item instanceof FAItem)
			{
				if (item instanceof FAItemBlock)
				{
					if (((FAItemBlock) item).faBlock instanceof IFluidBlock)
						RegisterFluidBlock((FAItemBlock) item);
					else
						RegisterItemBlock((BlockItem) item);

				} *//*else if (item instanceof MultiTypeItem)
				{
					MultiTypeItem<?> variantItem = (MultiTypeItem<?>) item;
					RegisterRenders(variantItem);
				}*//* *//*else if (item instanceof MultiStateItemBlock)
				{
					MultiStateItemBlock variantItem = (MultiStateItemBlock) item;
					RegisterRenders(variantItem);
				}*//* else
				{
					RegisterRender((FAItem) item, 0);
				}
			} else if (item instanceof BlockItem)
			{
				RegisterItemBlock((BlockItem) item);
			} else
			{
				// is a vanilla item, so we need to use a vanilla item method
				RegisterVanillaRender(item);
			}
		}*/
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterFluidBlock(FAItemBlock item)
	{
		// final ModelResourceLocation loc = new ModelResourceLocation(
				// new ResourceLocation(FactoryAutomation.MODID, item.GetMetaFilePath(0)), "inventory");
		//		ModelBakery.registerItemVariants(item.ToItem(), loc);
		//		ModelLoader.setCustomModelResourceLocation(item.ToItem(), 0, loc);
		//		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
		//		ModelLoader.setCustomStateMapper(item.getBlock(), new StateMapperBase()
		//		{
		//			@Override
		//			protected ModelResourceLocation getModelResourceLocation(BlockState state)
		//			{
		//				return new ModelResourceLocation(new ResourceLocation(FactoryAutomation.MODID, "fluids"),
		//						((IFluidBlock) item.getBlock()).getFluid().getName());
		//			}
		//		});
	}
	/*@SideOnly(Side.CLIENT)*/

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterRender(FAItem item, int meta)
	{
		// Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		final ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID, item.GetMetaFilePath(meta)), "inventory");

		// Log.LogInfo("The other model resource location", loc);

		//		ModelBakery.registerItemVariants(item.ToItem(), loc);
		//		ModelLoader.setCustomModelResourceLocation(item.ToItem(), meta, loc);
		//		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
	}

	private static void RegisterRenders(MultiTypeItem<?> item)
	{
		for (int meta = 0; meta < item.itemTypes.getEnumConstants().length; meta++)
		{
			// RegisterRender(item, meta);
		}
	}
	//	private static void RegisterRenders(MultiStateItemBlock item)
	//	{
	//		for (int meta = 0; meta < item.blockTypes.getEnumConstants().length; meta++)
	//		{
	//			RegisterRender(item, meta);
	//		}
	//	}


	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterVanillaRender(Item item)
	{
		// Log.LogInfo("Registering a vanilla Item class");
		// final ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
		//		ModelBakery.registerItemVariants(item, loc);
		//		ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		//		ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		// Log.LogInfo("Model resource location: ", loc);
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterItemBlock(BlockItem item)
	{
		if (item.getBlock() instanceof FABlock)
		{
			FABlock block = (FABlock) item.getBlock();
			// Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + block.GetMetaFilePath(0));

			final ModelResourceLocation loc = new ModelResourceLocation(
					new ResourceLocation(FactoryAutomation.MODID, block.GetMetaFilePath(0)), "inventory");

			// Log.LogInfo("The other model resource location", loc);

			//			ModelBakery.registerItemVariants(item, loc);
			//			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			//			ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		} else
		{
			RegisterVanillaRender(item);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegisterEvent event)
	{
		Init();

		event.register(ForgeRegistries.Keys.ITEMS, n -> items.forEach(i -> n.register(i.name(), i.obj())));

		// VanillaTweakHandler.RemoveItems(event);

		OreDictionaryHandler.registerOreDictionary();
	}
}
