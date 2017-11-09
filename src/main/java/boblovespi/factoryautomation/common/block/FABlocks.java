package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 11/9/2017.
 */
@Mod.EventBusSubscriber
public class FABlocks
{
	public static List<Block> blocks;

	public static FABlock concrete;

	public static void Init()
	{
		blocks = new ArrayList<>(100);

		concrete = new Concrete();
	}

	public static void RegisterRenders()
	{
		RegisterRender(concrete, 0);
	}

	public static void RegisterRender(FABlock block, int meta)
	{
		Log.getLogger().info("The other file path",
				FactoryAutomation.MODID + ":" + block.GetMetaFilePath(meta));

		ModelResourceLocation loc = new ModelResourceLocation(
				new ResourceLocation(FactoryAutomation.MODID,
						block.GetMetaFilePath(meta)), "inventory");

		Log.getLogger()
				.info("The other model resource location", loc.toString());

		if (block.IsItemBlock())
		{
			ModelBakery.registerItemVariants(
					Item.getItemFromBlock(block.ToBlock()), loc);
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(block.ToBlock()), meta, loc);
			ModelLoader.setCustomMeshDefinition(
					Item.getItemFromBlock(block.ToBlock()), stack -> loc);
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(blocks.toArray(new Block[] {}));
	}
}
