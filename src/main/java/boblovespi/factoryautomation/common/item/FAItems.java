package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.tools.*;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
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
	public static FAItem ingot;
	public static FAItem nugget;
	public static FAItem riceGrain;
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
	public static FAItem coalCoke;
	public static FAItem sheet;
	public static FAItem wearPlate;

	public static void Init()
	{
		if (!isInit.compareAndSet(false, true))
			return;

		items = new ArrayList<>(100);

		slag = new FABaseItem("slag", CreativeTabs.MATERIALS);
		ingot = new Ingot();
		sheet = new Sheet();
		nugget = new Nugget();
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
		wearPlate = new MultiTypeItem<>(
				"wear_plate", CreativeTabs.MISC, MachineTiers.class, "");
	}

	public static void RegisterItemRenders()
	{

		for (Item item : items)
		{
			Log.LogInfo("new item!");
			Log.LogInfo("Item unlocalized name", item.getUnlocalizedName());
			Log.LogInfo("item resource path",
						item.getRegistryName().getResourcePath());
			if (item instanceof FAItem)
			{
				if (item instanceof MultiTypeItem)
				{
					MultiTypeItem variantItem = (MultiTypeItem) item;
					RegisterRenders(variantItem);
				} else if (item instanceof MultiTypeItemBlock)
				{
					MultiTypeItemBlock variantItem = (MultiTypeItemBlock) item;
					RegisterRenders(variantItem);
				} else
				{
					RegisterRender((FAItem) item, 0);
				}
			} else
			{
				// is a vanilla item, so we need to use a vanilla item method
				RegisterVanillaRender(item);
			}
		}
	}

	private static void RegisterRender(FAItem item, int meta)
	{
		Log.LogInfo("The other file path",
					FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		final ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID,
									 item.GetMetaFilePath(meta)), "inventory");

		Log.LogInfo("The other model resource location", loc);

		ModelBakery.registerItemVariants(item.ToItem(), loc);
		ModelLoader.setCustomModelResourceLocation(item.ToItem(), meta, loc);
		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
	}

	private static void RegisterRenders(MultiTypeItem item)
	{
		for (int meta = 0;
			 meta < item.itemTypes.getEnumConstants().length; meta++)
		{

			RegisterRender(item, meta);
		}
	}

	private static void RegisterRenders(MultiTypeItemBlock item)
	{
		for (int meta = 0;
			 meta < item.blockTypes.getEnumConstants().length; meta++)
		{

			RegisterRender(item, meta);
		}
	}

	private static void RegisterVanillaRender(Item item)
	{
		Log.LogInfo("Registering a vanilla Item class");
		final ModelResourceLocation loc = new ModelResourceLocation(
				item.getRegistryName(), "inventory");
		ModelBakery.registerItemVariants(item, loc);
		ModelLoader.setCustomModelResourceLocation(item, 0, loc);
		ModelLoader.setCustomMeshDefinition(item, stack -> loc);
		Log.LogInfo("Model resource location: ", loc);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		Init();

		items.forEach(event.getRegistry()::register);
	}
}
