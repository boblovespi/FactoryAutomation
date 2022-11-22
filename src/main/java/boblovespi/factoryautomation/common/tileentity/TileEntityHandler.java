package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntityCable;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.tileentity.mechanical.*;
import boblovespi.factoryautomation.common.tileentity.pipe.TEPipe;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import boblovespi.factoryautomation.common.tileentity.processing.TETreetap;
import boblovespi.factoryautomation.common.tileentity.processing.TETumblingBarrel;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible;
import boblovespi.factoryautomation.common.tileentity.workbench.TEIronWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityHandler
{
	public static final DeferredRegister<BlockEntityType<?>> TE_TYPES = DeferredRegister.create(
			ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, MODID);

	public static List<Class<? extends BlockEntity>> tiles = new ArrayList<>(32);
	public static List<RegistryObjectWrapper<BlockEntityType<?>>> tileTypes = new ArrayList<>(32);
	public static RegistryObject<BlockEntityType<TECreativeMechanicalSource>> teCreativeMechanicalSource;
	public static RegistryObject<BlockEntityType<TEMultiblockPart>> teMultiblockPart;
	public static RegistryObject<BlockEntityType<TESolidFueledFirebox>> teSolidFueledFirebox;
	public static RegistryObject<BlockEntityType<TEHorseEngine>> teHorseEngine;
	public static RegistryObject<BlockEntityType<TEBasicCircuitCreator>> teBasicCircuitCreator;
	public static RegistryObject<BlockEntityType<TEBlastFurnaceController>> teBlastFurnaceController;
	public static RegistryObject<BlockEntityType<TESteelmakingFurnace>> teSteelmakingFurnace;
	public static RegistryObject<BlockEntityType<TEPipe>> tePipe;
	public static RegistryObject<BlockEntityType<TEPlacedBucket>> tePlacedBucket;
	public static RegistryObject<BlockEntityType<TEPump>> tePump;
	public static RegistryObject<BlockEntityType<TEBevelGear>> teBevelGear;
	public static RegistryObject<BlockEntityType<TEGearbox>> teGearbox;
	public static RegistryObject<BlockEntityType<TEHandCrank>> teHandCrank;
	public static RegistryObject<BlockEntityType<TEJawCrusher>> teJawCrusher;
	public static RegistryObject<BlockEntityType<TEMillstone>> teMillstone;
	public static RegistryObject<BlockEntityType<TELeatherBellows>> teLeatherBellows;
	public static RegistryObject<BlockEntityType<TEMotor>> teMotor;
	public static RegistryObject<BlockEntityType<TEPowerShaft>> tePowerShaft;
	public static RegistryObject<BlockEntityType<TETripHammerController>> teTripHammerController;
	public static RegistryObject<BlockEntityType<TEWaterwheel>> teWaterwheel;
	public static RegistryObject<BlockEntityType<TECampfire>> teCampfire;
	public static RegistryObject<BlockEntityType<TEChoppingBlock>> teChoppingBlock;
	public static RegistryObject<BlockEntityType<TETreetap>> teTreetap;
	public static RegistryObject<BlockEntityType<TEBrickCrucible>> teBrickCrucible;
	public static RegistryObject<BlockEntityType<TEPaperBellows>> tePaperBellows;
	public static RegistryObject<BlockEntityType<TEStoneCastingVessel>> teStoneCastingVessel;
	public static RegistryObject<BlockEntityType<TEStoneCrucible>> teStoneCrucible;
	public static RegistryObject<BlockEntityType<TEIronWorkbench>> teIronWorkbench;
	public static RegistryObject<BlockEntityType<TEStoneWorkbench>> teStoneWorkbench;
	public static RegistryObject<BlockEntityType<TileEntitySolarPanel>> teSolarPanel;
	public static RegistryObject<BlockEntityType<TileEntityCable>> teCable;
	public static RegistryObject<BlockEntityType<TEScrewPump>> teScrewPump;
	public static RegistryObject<BlockEntityType<TEWoodenTank>> teWoodenTank;
	public static RegistryObject<BlockEntityType<TESplitter>> teSplitter;
	public static RegistryObject<BlockEntityType<TETumblingBarrel>> teTumblingBarrel;

	static
	{
		// RegisterTileEntities();
	}

	public static void RegisterTileEntities()
	{
		teCreativeMechanicalSource = BuildType(TECreativeMechanicalSource::new, FABlocks.creativeMechanicalSource,
				"creative_mechanical_source");
		teMultiblockPart = BuildType(TEMultiblockPart::new, FABlocks.multiblockPart, "multiblock_part");
		teSolidFueledFirebox = BuildType(TESolidFueledFirebox::new, FABlocks.solidfueledfirebox,
				"solid_fueled_firebox");
		teHorseEngine = BuildType(TEHorseEngine::new, FABlocks.horseEngine, "horse_engine");
		teBasicCircuitCreator = BuildType(TEBasicCircuitCreator::new, FABlocks.chipCreator, "basic_circuit_creator");
		teBlastFurnaceController = BuildType(TEBlastFurnaceController::new, FABlocks.blastFurnaceController,
				"blast_furnace");
		teSteelmakingFurnace = BuildType(TESteelmakingFurnace::new, FABlocks.steelmakingFurnaceController,
				"steelmaking_furnace");
		tePipe = BuildType(TEPipe::new, List.of(FABlocks.ironPipe, FABlocks.woodPipe), "pipe");
		tePlacedBucket = BuildType(TEPlacedBucket::new, FABlocks.placedBucket, "placed_bucket");
		tePump = BuildType(TEPump::new, FABlocks.pump, "pump");
		teBevelGear = BuildType(TEBevelGear::new, FABlocks.bevelGear, "bevel_gear");
		teGearbox = BuildType(TEGearbox::new, FABlocks.gearbox, "gearbox");
		teHandCrank = BuildType(TEHandCrank::new, FABlocks.handCrank, "hand_crank");
		teJawCrusher = BuildType(TEJawCrusher::new, FABlocks.jawCrusher, "jaw_crusher");
		teMillstone = BuildType(TEMillstone::new, FABlocks.millstone, "millstone");
		teLeatherBellows = BuildType(TELeatherBellows::new, FABlocks.leatherBellows, "leather_bellows");
		teMotor = BuildType(TEMotor::new, FABlocks.motor, "motor");
		tePowerShaft = BuildType(TEPowerShaft::new, FABlocks.powerShafts, "power_shaft");
		teTripHammerController = BuildType(TETripHammerController::new, FABlocks.tripHammerController, "trip_hammer");
		teWaterwheel = BuildType(TEWaterwheel::new, FABlocks.waterwheel, "waterwheel");
		teCampfire = BuildType(TECampfire::new, FABlocks.campfire, "campfire");
		teChoppingBlock = BuildType(TEChoppingBlock::new, FABlocks.woodChoppingBlocks, "chopping_block");
		teTreetap = BuildType(TETreetap::new, FABlocks.treetap, "treetap");
		teBrickCrucible = BuildType(TEBrickCrucible::new, FABlocks.brickCrucible, "brick_crucible");
		tePaperBellows = BuildType(TEPaperBellows::new, FABlocks.paperBellows, "paper_bellows");
		teStoneCastingVessel = BuildType(TEStoneCastingVessel::new, FABlocks.stoneCastingVessel,
				"stone_casting_vessel");
		teStoneCrucible = BuildType(TEStoneCrucible::new, FABlocks.stoneCrucible, "stone_crucible");
		teIronWorkbench = BuildType(TEIronWorkbench::new, FABlocks.ironWorkbench, "iron_workbench");
		teStoneWorkbench = BuildType(TEStoneWorkbench::new, FABlocks.stoneWorkbench, "stone_workbench");
		teSolarPanel = BuildType(TileEntitySolarPanel::new, FABlocks.solarPanel, "solar_panel");
		teCable = BuildType(TileEntityCable::new, FABlocks.cable, "cable");
		teScrewPump = BuildType(TEScrewPump::new, FABlocks.screwPump, "screw_pump");
		teWoodenTank = BuildType(TEWoodenTank::new, FABlocks.woodenTank, "wooden_tank");
		teSplitter = BuildType(TESplitter::new, FABlocks.splitterIron, "splitter");
		teTumblingBarrel = BuildType(TETumblingBarrel::new, FABlocks.tumblingBarrel, "tumbling_barrel");
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> BuildType(
			BlockEntityType.BlockEntitySupplier<T> supplier,
			Block block, String name)
	{
		if (block == null)
			System.err.println("how wtf");
		var loc = new ResourceLocation(MODID, name);
		tileTypes.add(RegistryObjectWrapper.Of(name, BlockEntityType.Builder.of(supplier, block).build(null)));
		return RegistryObject.create(loc, ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, MODID);
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> BuildType(
			BlockEntityType.BlockEntitySupplier<T> supplier,
			List<Block> blocks, String name)
	{
		var loc = new ResourceLocation(MODID, name);
		tileTypes.add(RegistryObjectWrapper.Of(name, BlockEntityType.Builder.of(supplier, blocks.toArray(Block[]::new)).build(null)));
		return RegistryObject.create(loc, ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, MODID);
	}

	@SubscribeEvent
	public static void RegisterTypes(RegisterEvent event)
	{
		RegisterTileEntities();
		event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, n -> tileTypes.forEach(t -> t.Register(n)));
	}
}
