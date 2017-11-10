package boblovespi.factoryautomation;

import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.crafting.RecipieHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Willi on 11/8/2017.
 */
@Mod(modid = FactoryAutomation.MODID, name = FactoryAutomation.NAME, version = FactoryAutomation.VERSION)
public class FactoryAutomation
{

	public static final String MODID = "factoryautomation";
	public static final String VERSION = "alpha 1.0.0";
	public static final String NAME = "Factory Automation";
	public static final String COMMON_PROXY_CLASS = "boblovespi.factoryautomation.common.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "boblovespi.factoryautomation.client.ClientProxy";

	@SidedProxy(serverSide = COMMON_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;

	@Mod.Instance(MODID)
	public static FactoryAutomation instance;

	@Mod.EventHandler
	public void PreInit(FMLPreInitializationEvent Event)
	{
		Log.getLogger().info("Preinitialization");

		FAItems.Init();
		FABlocks.Init();


		proxy.RegisterRenders();

		Log.getLogger().info("Preinitialization end");
	}

	@Mod.EventHandler
	public void Init(FMLInitializationEvent Event)
	{
		Log.getLogger().info("Initialization");
		Log.LogInfo("Slag resource path", FAItems.slag.ToItem().getRegistryName());
		//RecipieHandler.RegisterCraftingRecipes();
		Log.getLogger().info("Initialization end");
	}

	@Mod.EventHandler
	public void PostInit(FMLPostInitializationEvent Event)
	{
		Log.getLogger().info("Postinitialization");
		Log.getLogger().info("Postinitialization end");
	}
}
