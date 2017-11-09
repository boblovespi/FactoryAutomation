package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
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
			if (item instanceof FAItem)
			{
				if (item instanceof IMultiVarientItem)
				{
					// TODO: add code to register all renders for the item
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
		Log.getLogger().info("The other file path",
				FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID,
						item.GetMetaFilePath(meta)), "inventory");

		Log.getLogger()
				.info("The other model resource location", loc.toString());

		ModelBakery.registerItemVariants(item.ToItem(), loc);
		ModelLoader.setCustomModelResourceLocation(item.ToItem(), meta, loc);
		ModelLoader.setCustomMeshDefinition(item.ToItem(), stack -> loc);
	}

	public static void RegisterVanillaRender(Item item)
	{
		ModelResourceLocation loc = new ModelResourceLocation(
				item.getRegistryName(), "inventory");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items.toArray(new Item[] {}));
	}
}
