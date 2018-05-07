package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.crucible.ClayCrucible;
import boblovespi.factoryautomation.common.item.metals.Ingot;
import boblovespi.factoryautomation.common.item.metals.Nugget;
import boblovespi.factoryautomation.common.item.metals.Sheet;
import boblovespi.factoryautomation.common.item.tools.*;
import boblovespi.factoryautomation.common.item.types.MachineTiers;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
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
	public static FAItem slag;
	public static MultiTypeItem<Metals> ingot;
	public static MultiTypeItem<Metals> nugget;
	public static MultiTypeItem<Metals> sheet;
	public static FAItem riceGrain;

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

	// more misc

	public static FAItem coalCoke;
	public static FAItem wearPlate;
	public static FAItem clayCrucible;
	public static FAItem diamondGravel;

	// workbench tools

	public static FAItem ironHammer;
	public static FAItem steelHammer;
	public static FAItem steelWrench;
	public static FAItem steelPinchers;
	public static FAItem sandpaper;

	// fluid canister

	public static FAItem fluidCanister;

	// crafting components

	public static MultiTypeItem<Gearbox.GearType> gear;
	public static FAItem glassLens;


	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		items = new ArrayList<>(100);

		slag = new FABaseItem("slag", CreativeTabs.MATERIALS);
		ingot = new Ingot();

		items.remove(ingot.GetItem(Metals.IRON));
		items.remove(ingot.GetItem(Metals.GOLD));

		sheet = new Sheet();
		nugget = new Nugget();

		items.remove(nugget.GetItem(Metals.IRON));
		items.remove(nugget.GetItem(Metals.GOLD));

		riceGrain = new RiceGrain();
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
		coalCoke = new FAFuel("coal_coke", CreativeTabs.MATERIALS, 2000);
		wearPlate = new MultiTypeItem<>("wear_plate", CreativeTabs.MISC, MachineTiers.class, "");

		clayCrucible = new ClayCrucible();

		diamondGravel = new CauldronCleanable("diamond_gravel", FACreativeTabs.resources, new ItemStack(Items.DIAMOND));

		ironHammer = new WorkbenchToolItem("iron_hammer", 8, -3.7f, Item.ToolMaterial.IRON);
		steelHammer = new WorkbenchToolItem("steel_hammer", 12, -3.7f, ToolMaterials.steelMaterial);
		steelWrench = new WorkbenchToolItem("steel_wrench", 0, 0, ToolMaterials.steelMaterial);
		steelPinchers = new WorkbenchToolItem("steel_pinchers", 0, 0, ToolMaterials.steelMaterial);
		sandpaper = new WorkbenchToolItem("sandpaper", 0, 0, Item.ToolMaterial.WOOD);

		// fluidCanister = new FluidCanister("fluid_canister", 3000);

		gear = new Gear();
		glassLens = new FABaseItem("glass_lens", FACreativeTabs.crafting);
	}

	public static void RegisterItemRenders()
	{
		for (Item item : items)
		{
			Log.LogInfo("new item!");
			Log.LogInfo("Item unlocalized name", item.getUnlocalizedName());
			Log.LogInfo("item resource path", item.getRegistryName().getResourcePath());
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
		Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		final ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID, item.GetMetaFilePath(meta)), "inventory");

		Log.LogInfo("The other model resource location", loc);

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
		Log.LogInfo("Registering a vanilla Item class");
		final ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelBakery.registerItemVariants(item, loc);
		ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		Log.LogInfo("Model resource location: ", loc);
	}

	@SuppressWarnings("MethodCallSideOnly")
	private static void RegisterItemBlock(ItemBlock item)
	{
		if (item.getBlock() instanceof FABlock)
		{
			FABlock block = (FABlock) item.getBlock();
			Log.LogInfo("The other file path", FactoryAutomation.MODID + ":" + block.GetMetaFilePath(0));

			final ModelResourceLocation loc = new ModelResourceLocation(
					new ResourceLocation(FactoryAutomation.MODID, block.GetMetaFilePath(0)), "inventory");

			Log.LogInfo("The other model resource location", loc);

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
