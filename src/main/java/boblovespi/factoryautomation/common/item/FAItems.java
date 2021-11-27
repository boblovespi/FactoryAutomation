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
import boblovespi.factoryautomation.common.util.Log;
import boblovespi.factoryautomation.common.util.SoundHandler;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static boblovespi.factoryautomation.common.item.tools.ToolMaterial.*;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FAItems
{
	private static final AtomicBoolean isInit = new AtomicBoolean(false);
	public static final List<Item> items = new ArrayList<>(100);

	// metal resources

	public static MultiTypeItem<Metals> ingot;
	public static MultiTypeItem<Metals> nugget;
	public static MultiTypeItem<Metals> sheet;
	public static MultiTypeItem<Metals> coin;
	public static MultiTypeItem<Metals> rod;
	public static FAItem diamondCoin;

	// metallurgy misc

	public static FAItem slag;
	public static FAItem coalCoke;
	public static FAItem ironShard;
	public static MultiTypeItem<TallowForms> pigTallowParts;
	public static MultiTypeItem<TallowForms> pigTallowMolds;
	public static MultiTypeItem<TallowForms> firedMolds;

	// ore processing forms

	public static MultiTypeItem<OreForms> processedMagnetite;
	public static MultiTypeItem<OreForms> processedLimonite;

	// food

	public static FAItem riceGrain;
	public static FAItem toastedBread;
	public static FAItem wheatFlour;
	public static FAItem[] iceCream;
	public static FAItem pancake;
	public static FAItem honeyPancake;

	// resources

	public static FAItem diamondGravel;
	public static FAItem stoneDust;
	public static FAItem porcelainClay;
	public static FAItem siliconQuartz;
	public static FAItem ash;
	public static FAItem liquidGlycerin;
	public static FAItem dryGlycerin;
	public static FAItem acidPowder;
	public static FAItem rawRubber;
	public static FAItem rubber;
	public static FAItem graphite;
	public static FAItem terraclay;
	public static FAItem terraclayBrick;
	public static FAItem plantFiber;
	public static FAItem pigTallow;

	// crafting components

	public static MultiTypeItem<Gearbox.GearType> gear;
	public static FAItem glassLens;
	public static FAItem airPiston;
	public static FAItem bronzeFlywheel;
	public static FAItem stirlingGeneratorCore;

	// crafting parts

	public static FAItem screw;

	// electrical parts

	public static FAItem copperWire;
	public static FAItem basicChip;
	public static FAItem circuitFrame;
	public static FAItem dataprintCircuit;

	// regular tools

	public static FAItem bronzePickaxe;
	public static FAItem bronzeAxe;
	public static FAItem bronzeHoe;
	public static FAItem bronzeShovel;
	public static FAItem bronzeSword;

	public static FAItem steelPickaxe;
	public static FAItem steelAxe;
	public static FAItem steelHoe;
	public static FAItem steelShovel;
	public static FAItem steelSword;

	public static FAItem copperPickaxe;
	public static FAItem copperAxe;
	public static FAItem copperHoe;
	public static FAItem copperShovel;
	public static FAItem copperSword;

	public static FAItem flintPickaxe;

	public static FAItem choppingBlade;
	public static FAItem firebow;

	// workbench tools

	public static FAItem copperHammer;
	public static FAItem ironHammer;
	public static FAItem steelHammer;
	public static FAItem steelWrench;
	public static FAItem steelPinchers;
	public static FAItem sandpaper;
	public static FAItem advancedFlintAndSteel;

	// misc tools

	public static FAItem clayCrucible;
	public static MultiTypeItem<MachineTiers> wearPlate;

	// fluid canister

	public static FAItem fluidCanister;

	// misc

	public static FAItem factoryDisc;
	public static FAItem meterDisc;

	// guidebook

	// public static FAItem guidebook;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		// metal resources

		ingot = new Ingot();
		items.remove(ingot.GetItem(Metals.IRON));
		items.remove(ingot.GetItem(Metals.GOLD));
		nugget = new Nugget();
		items.remove(nugget.GetItem(Metals.IRON));
		items.remove(nugget.GetItem(Metals.GOLD));
		sheet = new Sheet();
		coin = new MetalItem("coin");
		diamondCoin = new FABaseItem("coin_diamond", ItemGroup.TAB_MISC
		);
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

		// foods

		riceGrain = new RiceGrain();
		toastedBread = new FAFood(
				"toasted_bread", 5, 4, 32, false, false, Collections.emptyList(), Collections.emptyList());
		wheatFlour = new FABaseItem("wheat_flour", FAItemGroups.resources);
		iceCream = new FAItem[IceCreams.values().length];
		for (int i = 0; i < iceCream.length; i++)
		{
			iceCream[i] = new FAFood("ice_cream_" + IceCreams.values()[i].name().toLowerCase(), 8, 12, 32, false, true,
					IceCreams.values()[i].GetPotionEffects(), Collections.singletonList(1f));
		}
		pancake = new FABaseItem("pancake", Prop().tab(ItemGroup.TAB_FOOD).food(new Food.Builder().nutrition(5).saturationMod(0.8f).build()));
		honeyPancake = new FABaseItem("honey_pancake", Prop().tab(ItemGroup.TAB_FOOD).food(new Food.Builder().nutrition(11).saturationMod(1f).build()));

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

		flintPickaxe = new FAPickaxe(ItemTier.WOOD, "flint_pickaxe");

		choppingBlade = new FAAxe(flintMaterial, "chopping_blade");
		firebow = new Firebow();

		// workbench tools

		copperHammer = new Hammer("copper_hammer", 5, -3.7f, ToolMaterial.copperMaterial);
		ironHammer = new Hammer("iron_hammer", 8, -3.7f, ItemTier.IRON);
		steelHammer = new Hammer("steel_hammer", 12, -3.7f, ToolMaterial.steelMaterial);
		steelWrench = new Wrench("steel_wrench", 0, 0, ToolMaterial.steelMaterial);
		steelPinchers = new WorkbenchToolItem(
				"steel_pinchers", 0, 0, ToolMaterial.steelMaterial, Prop(), FAToolTypes.NONE);
		sandpaper = new WorkbenchToolItem("sandpaper", 0, 0, ItemTier.WOOD, Prop(), FAToolTypes.NONE);
		advancedFlintAndSteel = new AdvancedFlintAndSteel();

		// misc tools

		wearPlate = new MultiTypeItem<>("wear_plate", MachineTiers.class, "", Prop());
		clayCrucible = new ClayCrucible();

		// fluidCanister = new FluidCanister("fluid_canister", 3000);

		// guidebook

		// guidebook = new Guidebook();

		// misc

		factoryDisc = new MusicDisc("disc_factory", 15, () -> SoundHandler.factoryDisc, Prop().tab(ItemGroup.TAB_MISC));
		meterDisc = new MusicDisc("disc_meter", 15, () -> SoundHandler.meterDisc, Prop().tab(ItemGroup.TAB_MISC));
	}

	public static Item.Properties Prop()
	{
		return new Item.Properties();
	}

	public static Item.Properties Building()
	{
		return Prop().tab(ItemGroup.TAB_BUILDING_BLOCKS);
	}

	public static void RegisterItemRenders()
	{
		for (Item item : items)
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

				} /*else if (item instanceof MultiTypeItem)
				{
					MultiTypeItem<?> variantItem = (MultiTypeItem<?>) item;
					RegisterRenders(variantItem);
				}*/ /*else if (item instanceof MultiStateItemBlock)
				{
					MultiStateItemBlock variantItem = (MultiStateItemBlock) item;
					RegisterRenders(variantItem);
				}*/ else
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
		}
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterFluidBlock(FAItemBlock item)
	{
		final ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID, item.GetMetaFilePath(0)), "inventory");
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
			RegisterRender(item, meta);
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
		final ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
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
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		Init();

		items.forEach(event.getRegistry()::register);

		// VanillaTweakHandler.RemoveItems(event);

		OreDictionaryHandler.registerOreDictionary();
	}
}
