package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

	}

	public static void RegisterRenders()
	{

	}

	public static void RegisterRender(FAItem item, int meta)
	{
		Log.getLogger().info("The other file path",
				FactoryAutomation.MODID + ":" + item.GetMetaFilePath(meta));

		ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID,
						item.GetMetaFilePath(0)), "inventory");

		Log.getLogger()
				.info("The other model resource location", loc.toString());

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item.ToItem(), meta, loc);
	}

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items.toArray(new Item[] {}));
	}
}
