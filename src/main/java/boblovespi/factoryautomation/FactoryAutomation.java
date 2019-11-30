package boblovespi.factoryautomation;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.api.pollution.CapabilityPollutedChunk;
import boblovespi.factoryautomation.client.ClientProxy;
import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.ServerProxy;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.config.ConfigFields;
import boblovespi.factoryautomation.common.fluid.Fluids;
import boblovespi.factoryautomation.common.handler.*;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.ToolMaterial;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructures;
import boblovespi.factoryautomation.common.network.PacketHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TETripHammerController;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEWaterwheel;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible;
import boblovespi.factoryautomation.common.util.FuelHandler;
import boblovespi.factoryautomation.common.util.Log;
import boblovespi.factoryautomation.common.util.ModCompatHandler;
import boblovespi.factoryautomation.common.util.TooltipHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created by Willi on 11/8/2017.
 * main mod class
 */
@Mod(FactoryAutomation.MODID/*, name = FactoryAutomation.NAME, version = FactoryAutomation.VERSION/*, guiFactory = FactoryAutomation.GUI_FACTORY*/)
public class FactoryAutomation
{
	public static final String MODID = "factoryautomation";
	public static final String VERSION = "Alpha 0.2.2";
	public static final String NAME = "Factory Automation";
	public static final String SERVER_PROXY_CLASS = "boblovespi.factoryautomation.common.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "boblovespi.factoryautomation.client.ClientProxy";
	public static final String GUI_FACTORY = "net.minecraftforge.fml.client.DefaultGuiFactory";
	// @Mod.Instance(MODID)
	public static FactoryAutomation instance = new FactoryAutomation();
	// @SidedProxy(serverSide = SERVER_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
	@SuppressWarnings("Convert2MethodRef")
	public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	//	static
	//	{
	//		FluidRegistry.enableUniversalBucket();
	//	}

	public FactoryAutomation()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ClientSetup);
	}

	private void Setup(FMLCommonSetupEvent event)
	{
		Log.getLogger().info("Preinitialization");

		ConfigFields.AddClass(VanillaTweakHandler.class);
		ConfigFields.AddClass(ToolMaterial.class);

		// ConfigManager.sync(MODID, Config.Type.INSTANCE);

		CapabilityPollutedChunk.Register();
		CapabilityMechanicalUser.Register();
		CapabilityHeatUser.Register();
		CapabilityBellowsUser.Register();

		// FAConfig.PreInit();
		PacketHandler.CreateChannel(MODID);
		proxy.PreInit();

		Fluids.RegisterFluids();

		FAItems.Init();
		FABlocks.Init();

		// proxy.RegisterRenders();

		Log.getLogger().info("Preinitialization end");
	}

	private void ClientSetup(FMLClientSetupEvent event)
	{
		proxy.RegisterRenders();
	}

	@SuppressWarnings("unused")
	// @Mod.EventHandler
	public void Init()
	{
		Log.getLogger().info("Initialization");
		proxy.Init();
		// GameRegistry.registerWorldGenerator(new WorldGenHandler(), 0);
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

		MultiblockHandler.Register(
				TEStoneCrucible.MULTIBLOCK_ID,
				new MultiblockStructurePattern(MultiblockStructures.stoneFoundry, new int[] { 0, 1, 0 }));

		MultiblockHandler.Register(
				TEBrickCrucible.MULTIBLOCK_ID,
				new MultiblockStructurePattern(MultiblockStructures.brickFoundry, new int[] { 0, 1, 0 }));

		MultiblockHandler.Register(
				TEWaterwheel.MULTIBLOCK_ID,
				new MultiblockStructurePattern(MultiblockStructures.waterwheel, new int[] { 0, 2, 2 }));

		MinecraftForge.EVENT_BUS.register(WorldTickHandler.GetInstance());
		LootTableHandler.RegisterTables();
		ModCompatHandler.Init();
	}

	@SuppressWarnings("unused")
	// @Mod.EventHandler
	public void PostInit(/*FMLPostInitializationEvent Event*/)
	{
		Log.getLogger().info("Postinitialization");

		VanillaTweakHandler.TweakToolLevels();
		VanillaTweakHandler.TweakMiningLevels();

		TooltipHandler.RegisterTooltips();

		RecipeHandler.RemoveSmeltingRecipes();

		Log.getLogger().info("Postinitialization end");
	}
}
