package boblovespi.factoryautomation;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.pollution.CapabilityPollutedChunk;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.config.ConfigFields;
import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.handler.VanillaTweakHandler;
import boblovespi.factoryautomation.common.handler.WorldTickHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.ToolMaterials;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructures;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import boblovespi.factoryautomation.common.util.FuelHandler;
import boblovespi.factoryautomation.common.util.Log;
import boblovespi.factoryautomation.common.util.TooltipHandler;
import boblovespi.factoryautomation.common.worldgen.WorldGenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Willi on 11/8/2017.
 * main mod class
 */
@Mod(modid = FactoryAutomation.MODID, name = FactoryAutomation.NAME, version = FactoryAutomation.VERSION/*, guiFactory = FactoryAutomation.GUI_FACTORY*/)
public class FactoryAutomation
{
	public static final String MODID = "factoryautomation";
	public static final String VERSION = "Alpha 0.0.11";
	public static final String NAME = "Factory Automation";
	public static final String SERVER_PROXY_CLASS = "boblovespi.factoryautomation.common.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "boblovespi.factoryautomation.client.ClientProxy";
	public static final String GUI_FACTORY = "net.minecraftforge.fml.client.DefaultGuiFactory";
	@Mod.Instance(MODID)
	public static FactoryAutomation instance = new FactoryAutomation();
	@SidedProxy(serverSide = SERVER_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void PreInit(FMLPreInitializationEvent Event)
	{
		Log.getLogger().info("Preinitialization");

		ConfigFields.AddClass(VanillaTweakHandler.class);
		ConfigFields.AddClass(ToolMaterials.class);

		ConfigManager.sync(MODID, Config.Type.INSTANCE);

		CapabilityPollutedChunk.Register();
		CapabilityMechanicalUser.Register();
		CapabilityHeatUser.Register();

		// FAConfig.PreInit();
		PacketHandler.CreateChannel(MODID);
		proxy.PreInit();

		Fluids.RegisterFluids();

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
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 0);
		FuelHandler.RegisterFuels();
		// OreDictionaryHandler.registerOreDictionary();
		Log.LogInfo("Slag resource path", FAItems.slag.ToItem().getRegistryName());
		TileEntityHandler.RegisterTileEntities();

		Log.getLogger().info("Initialization end");

		MultiblockHandler.Register(
				"blast_furnace",
				new MultiblockStructurePattern(MultiblockStructures.blastFurnace, new int[] { 0, 0, 1 }));

		MultiblockHandler.Register(
				"steelmaking_furnace",
				new MultiblockStructurePattern(MultiblockStructures.steelmakingFurnace, new int[] { 1, 1, 2 }));

		MultiblockHandler.Register(
				TETripHammerController.MULTIBLOCK_ID,
				new MultiblockStructurePattern(MultiblockStructures.tripHammer, new int[] { 0, 0, 0 }));

		MinecraftForge.EVENT_BUS.register(WorldTickHandler.GetInstance());
	}

	@SuppressWarnings("unused")
	@Mod.EventHandler
	public void PostInit(FMLPostInitializationEvent Event)
	{
		Log.getLogger().info("Postinitialization");

		VanillaTweakHandler.TweakToolLevels();
		VanillaTweakHandler.TweakMiningLevels();

		TooltipHandler.RegisterTooltips();

		Log.getLogger().info("Postinitialization end");
	}
}
