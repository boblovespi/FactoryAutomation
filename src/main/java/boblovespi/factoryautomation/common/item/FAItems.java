package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.crucible.ClayCrucible;
import boblovespi.factoryautomation.common.item.metals.Ingot;
import boblovespi.factoryautomation.common.item.metals.MetalItem;
import boblovespi.factoryautomation.common.item.metals.Nugget;
import boblovespi.factoryautomation.common.item.metals.Sheet;
import boblovespi.factoryautomation.common.item.ores.OreForms;
import boblovespi.factoryautomation.common.item.ores.ProcessedOre;
import boblovespi.factoryautomation.common.item.tools.*;
import boblovespi.factoryautomation.common.item.types.MachineTiers;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.bronzeMaterial;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.copperMaterial;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.steelMaterial;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod.EventBusSubscriber
public class FAItems
{
	@GameRegistry.ObjectHolder("factoryautomation:ore_metal")
	public static final Item metalOre = null;

	private static final AtomicBoolean isInit = new AtomicBoolean(false);
	public static List<Item> items;

	// metal resources

	public static MultiTypeItem<Metals> ingot;
	public static MultiTypeItem<Metals> nugget;
	public static MultiTypeItem<Metals> sheet;
	public static MultiTypeItem<Metals> coin;

	// metallurgy misc

	public static FAItem slag;
	public static FAItem coalCoke;

	// ore processing forms

	public static MultiTypeItem<OreForms> processedMagnetite;

	// food

	public static FAItem riceGrain;

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

	// crafting components

	public static MultiTypeItem<Gearbox.GearType> gear;
	public static FAItem glassLens;

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

	// workbench tools

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

	// guidebook

	public static FAItem guidebook;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		items = new ArrayList<>(100);

		// metal resources

		ingot = new Ingot();
		items.remove(ingot.GetItem(Metals.IRON));
		items.remove(ingot.GetItem(Metals.GOLD));
		nugget = new Nugget();
		items.remove(nugget.GetItem(Metals.IRON));
		items.remove(nugget.GetItem(Metals.GOLD));
		sheet = new Sheet();
		coin = new MetalItem("coin");

		// metallurgy misc

		slag = new FABaseItem("slag", FACreativeTabs.metallurgy);
		coalCoke = new FAFuel("coal_coke", FACreativeTabs.metallurgy, 2000);

		// ore processing forms

		processedMagnetite = new ProcessedOre("magnetite");

		// food

		riceGrain = new RiceGrain();

		// resources

		diamondGravel = new CauldronCleanable("diamond_gravel", FACreativeTabs.resources, new ItemStack(Items.DIAMOND));
		stoneDust = new FABaseItem("stone_dust", FACreativeTabs.resources);
		porcelainClay = new FABaseItem("porcelain_clay", FACreativeTabs.resources);
		siliconQuartz = new FABaseItem("silicon_quartz_crystal", FACreativeTabs.resources);
		ash = new FABaseItem("ash", FACreativeTabs.resources);
		liquidGlycerin = new FABaseItem("liquid_glycerin", FACreativeTabs.resources);
		dryGlycerin = new FABaseItem("dry_glycerin", FACreativeTabs.resources);
		acidPowder = new FABaseItem("acid_powder", FACreativeTabs.resources);
		rawRubber = new FABaseItem("raw_rubber", FACreativeTabs.resources);
		rubber = new FABaseItem("rubber", FACreativeTabs.resources);
		graphite = new FABaseItem("graphite", FACreativeTabs.resources);

		// crafting components

		gear = new Gear();
		glassLens = new FABaseItem("glass_lens", FACreativeTabs.crafting);

		// crafting parts

		screw = new WorkbenchPartItem("screw", FACreativeTabs.crafting);

		// electrical parts

		copperWire = new FABaseItem("copper_wire", FACreativeTabs.electrical);
		basicChip = new FABaseItem("basic_chip", FACreativeTabs.electrical);
		circuitFrame = new FABaseItem("circuit_frame", FACreativeTabs.electrical);
		dataprintCircuit = new FABaseItem("dataprint_circuit", FACreativeTabs.electrical);

		// regular tools

		bronzePickaxe = new FAPickaxe(bronzeMaterial, "bronze_pickaxe");
		bronzeAxe = new FAAxe(bronzeMaterial, "bronze_axe");
		bronzeHoe = new FAHoe(bronzeMaterial, "bronze_hoe");
		bronzeShovel = new FAShovel(bronzeMaterial, "bronze_shovel");
		bronzeSword = new FASword(bronzeMaterial, "bronze_sword");

		steelAxe = new FAAxe(steelMaterial, "steel_axe");
		steelHoe = new FAHoe(steelMaterial, "steel_hoe");
		steelShovel = new FAShovel(steelMaterial, "steel_shovel");
		steelSword = new FASword(steelMaterial, "steel_sword");
		steelPickaxe = new FAPickaxe(steelMaterial, "steel_pickaxe");

		copperAxe = new FAAxe(copperMaterial, "copper_axe");
		copperHoe = new FAHoe(copperMaterial, "copper_hoe");
		copperShovel = new FAShovel(copperMaterial, "copper_shovel");
		copperSword = new FASword(copperMaterial, "copper_sword");
		copperPickaxe = new FAPickaxe(copperMaterial, "copper_pickaxe");

		// workbench tools

		ironHammer = new Hammer("iron_hammer", 8, -3.7f, Item.ToolMaterial.IRON);
		steelHammer = new Hammer("steel_hammer", 12, -3.7f, ToolMaterials.steelMaterial);
		steelWrench = new Wrench("steel_wrench", 0, 0, ToolMaterials.steelMaterial);
		steelPinchers = new WorkbenchToolItem("steel_pinchers", 0, 0, ToolMaterials.steelMaterial);
		sandpaper = new WorkbenchToolItem("sandpaper", 0, 0, Item.ToolMaterial.WOOD);
		advancedFlintAndSteel = new AdvancedFlintAndSteel();

		// misc tools

		wearPlate = new MultiTypeItem<>("wear_plate", FACreativeTabs.mechanical, MachineTiers.class, "");
		clayCrucible = new ClayCrucible();

		// fluidCanister = new FluidCanister("fluid_canister", 3000);

		// guidebook

		guidebook = new Guidebook();
	}

	public static void RegisterItemRenders()
	{
		for (Item item : items)
		{
			Log.LogInfo("new item!", item.getRegistryName().getResourcePath());
			// Log.LogInfo("Item unlocalized name", item.getUnlocalizedName());
			// Log.LogInfo("item resource path", item.getRegistryName().getResourcePath());
			if (item instanceof FAItem)
			{
				if (item instanceof FAItemBlock)
				{
					if (((FAItemBlock) item).faBlock instanceof IFluidBlock)
						RegisterFluidBlock((FAItemBlock) item);
					else
						RegisterItemBlock((ItemBlock) item);

				} else if (item instanceof MultiTypeItem)
				{
					MultiTypeItem variantItem = (MultiTypeItem) item;
					RegisterRenders(variantItem);
				} else if (item instanceof MultiStateItemBlock)
				{
					MultiStateItemBlock variantItem = (MultiStateItemBlock) item;
					RegisterRenders(variantItem);
				} else
				{
					RegisterRender((FAItem) item, 0);
				}
			} else if (item instanceof ItemBlock)
			{
				RegisterItemBlock((ItemBlock) item);
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
		ModelBakery.registerItemVariants(item.ToItem(), loc);
		ModelLoader.setCustomModelResourceLocation(item.ToItem(), 0, loc);
		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
		ModelLoader.setCustomStateMapper(item.getBlock(), new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation(
						new ResourceLocation(FactoryAutomation.MODID, "fluids"),
						((IFluidBlock) item.getBlock()).getFluid().getName());
			}
		});
	}

	/*@SideOnly(Side.CLIENT)*/
	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterRender(FAItem item, int meta)
	{
		// Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		final ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID, item.GetMetaFilePath(meta)), "inventory");

		// Log.LogInfo("The other model resource location", loc);

		ModelBakery.registerItemVariants(item.ToItem(), loc);
		ModelLoader.setCustomModelResourceLocation(item.ToItem(), meta, loc);
		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
	}

	private static void RegisterRenders(MultiTypeItem item)
	{
		for (int meta = 0; meta < item.itemTypes.getEnumConstants().length; meta++)
		{
			RegisterRender(item, meta);
		}
	}

	private static void RegisterRenders(MultiStateItemBlock item)
	{
		for (int meta = 0; meta < item.blockTypes.getEnumConstants().length; meta++)
		{
			RegisterRender(item, meta);
		}
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterVanillaRender(Item item)
	{
		// Log.LogInfo("Registering a vanilla Item class");
		final ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelBakery.registerItemVariants(item, loc);
		ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		// Log.LogInfo("Model resource location: ", loc);
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterItemBlock(ItemBlock item)
	{
		if (item.getBlock() instanceof FABlock)
		{
			FABlock block = (FABlock) item.getBlock();
			// Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + block.GetMetaFilePath(0));

			final ModelResourceLocation loc = new ModelResourceLocation(
					new ResourceLocation(FactoryAutomation.MODID, block.GetMetaFilePath(0)), "inventory");

			// Log.LogInfo("The other model resource location", loc);

			ModelBakery.registerItemVariants(item, loc);
			ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			ModelLoader.setCustomMeshDefinition(item, stack -> loc);
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
	}
}
