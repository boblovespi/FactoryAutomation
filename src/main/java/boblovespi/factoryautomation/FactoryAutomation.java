package boblovespi.factoryautomation;

import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.config.Config;
import boblovespi.factoryautomation.common.handler.OreDictionaryHandler;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructures;
import boblovespi.factoryautomation.common.util.Log;
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
	@Mod.Instance(MODID)
	public static FactoryAutomation instance = new FactoryAutomation();
	@SidedProxy(serverSide = COMMON_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	private static CommonProxy proxy;

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
		proxy.Init();
		OreDictionaryHandler.registerOreDictionary();
		Log.LogInfo("Slag resource path",
					FAItems.slag.ToItem().getRegistryName());
		TileEntityHandler.RegisterTileEntities();
		Log.getLogger().info("Initialization end");

		MultiblockHandler.Register("blast_furnace",
								   new MultiblockStructurePattern(
										   MultiblockStructures.blastFurnace,
										   new int[] { 0, 0, 1 }));

		//MultiblockHandler.Register("steelmaking_furnace", );
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void PostInit(FMLPostInitializationEvent Event)
	{
		Log.getLogger().info("Postinitialization");
		Log.getLogger().info("Postinitialization end");
	}
}
