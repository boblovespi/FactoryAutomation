package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod.EventBusSubscriber
public class FAItems
{
	public static List<Item> items;

	public static FAItem slag;

	public static void Init()
	{
		items = new ArrayList<>(100);

		slag = new FABaseItem("slag", CreativeTabs.MATERIALS);
	}

	public static void RegisterRenders()
	{
		for (Item item : items)
		{
			Log.LogInfo("new item!");
			Log.LogInfo("Item unlocalized name", item.getUnlocalizedName());
			Log.LogInfo("item resource path",
					item.getRegistryName().getResourcePath());
			if (item instanceof ItemBlock)
			{
				Log.LogInfo("Is an itemblock, not taking any action");
				continue;
			}
			else if (item instanceof FAItem)
			{
				if (item instanceof IMultiVariantItem)
				{
					// TODO: add code to register all renders for the item
					IMultiVariantItem variantItem = (IMultiVariantItem) item;
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

	public static void RegisterRender(FAItem item, int meta)
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

	public static void RegisterVanillaRender(Item item)
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
		items.forEach(event.getRegistry()::register);
	}
}
