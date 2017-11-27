package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.gui.GuiHandler;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.config.Config;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		FAItems.RegisterItemRenders();
		FABlocks.RegisterRenders();
	}

	@Override
	public void RegisterRenders()
	{

	}

	@Override
	public void PreInit()
	{
		super.PreInit();
		// ModelLoaderRegistry.registerLoader(new ObjModelLoader());
		OBJLoader.INSTANCE.addDomain("factoryautomation");
		Config.ClientPreInit();
	}

	@Override
	public void Init()
	{
		super.Init();
		NetworkRegistry.INSTANCE.registerGuiHandler(FactoryAutomation.instance,
				new GuiHandler());
	}
}
