package boblovespi.factoryautomation;

import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.config.Config;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.init.RecipeHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Log;
import boblovespi.factoryautomation.common.init.OreDictionaryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod(modid = FactoryAutomation.MODID, name = FactoryAutomation.NAME, version = FactoryAutomation.VERSION, guiFactory = FactoryAutomation.GUI_FACTORY)
public class FactoryAutomation
{

	public static final String MODID = "factoryautomation";
	public static final String VERSION = "alpha 1.0.0";
	public static final String NAME = "Factory Automation";
	public static final String COMMON_PROXY_CLASS = "boblovespi.factoryautomation.common.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "boblovespi.factoryautomation.client.ClientProxy";
	public static final String GUI_FACTORY = "boblovespi.factoryautomation.common.config.ConfigGuiFactory";

	@SidedProxy(serverSide = COMMON_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	private static CommonProxy proxy;

	@Mod.Instance(MODID)
	public static FactoryAutomation instance;

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void PreInit(FMLPreInitializationEvent Event)
	{
		Log.getLogger().info("Preinitialization");

		Config.PreInit();
		proxy.PreInit();

		FAItems.Init();
		FABlocks.Init();

		proxy.RegisterRenders();

		Log.getLogger().info("Preinitialization end");
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void Init(FMLInitializationEvent Event)
	{
		Log.getLogger().info("Initialization");
        OreDictionaryHandler.registerOreDictionary();
		Log.LogInfo("Slag resource path",
				FAItems.slag.ToItem().getRegistryName());
		RecipeHandler.RegisterCraftingRecipes();
		Log.getLogger().info("Initialization end");
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void PostInit(FMLPostInitializationEvent Event)
	{
		Log.getLogger().info("Postinitialization");
		Log.getLogger().info("Postinitialization end");
	}
}
