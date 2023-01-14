package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.container.StringIntArray;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntityCable;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.tileentity.mechanical.*;
import boblovespi.factoryautomation.common.tileentity.pipe.TEPipe;
import boblovespi.factoryautomation.common.tileentity.processing.*;
import boblovespi.factoryautomation.common.tileentity.smelting.*;
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
import java.util.function.Supplier;

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
	public static Supplier<BlockEntityType<TECreativeMechanicalSource>> teCreativeMechanicalSource;
	public static Supplier<BlockEntityType<TEMultiblockPart>> teMultiblockPart;
	public static Supplier<BlockEntityType<TESolidFueledFirebox>> teSolidFueledFirebox;
	public static Supplier<BlockEntityType<TEHorseEngine>> teHorseEngine;
	public static Supplier<BlockEntityType<TEBasicCircuitCreator>> teBasicCircuitCreator;
	public static Supplier<BlockEntityType<TEBlastFurnaceController>> teBlastFurnaceController;
	public static Supplier<BlockEntityType<TESteelmakingFurnace>> teSteelmakingFurnace;
	public static Supplier<BlockEntityType<TEPipe>> tePipe;
	public static Supplier<BlockEntityType<TEPlacedBucket>> tePlacedBucket;
	public static Supplier<BlockEntityType<TEPump>> tePump;
	public static Supplier<BlockEntityType<TEBevelGear>> teBevelGear;
	public static Supplier<BlockEntityType<TEGearbox>> teGearbox;
	public static Supplier<BlockEntityType<TEHandCrank>> teHandCrank;
	public static Supplier<BlockEntityType<TEJawCrusher>> teJawCrusher;
	public static Supplier<BlockEntityType<TEMillstone>> teMillstone;
	public static Supplier<BlockEntityType<TELeatherBellows>> teLeatherBellows;
	public static Supplier<BlockEntityType<TEMotor>> teMotor;
	public static Supplier<BlockEntityType<TEPowerShaft>> tePowerShaft;
	public static Supplier<BlockEntityType<TETripHammerController>> teTripHammerController;
	public static Supplier<BlockEntityType<TEWaterwheel>> teWaterwheel;
	public static Supplier<BlockEntityType<TECampfire>> teCampfire;
	public static Supplier<BlockEntityType<TEChoppingBlock>> teChoppingBlock;
	public static Supplier<BlockEntityType<TETreetap>> teTreetap;
	public static Supplier<BlockEntityType<TEBrickCrucible>> teBrickCrucible;
	public static Supplier<BlockEntityType<TEPaperBellows>> tePaperBellows;
	public static Supplier<BlockEntityType<TEStoneCastingVessel>> teStoneCastingVessel;
	public static Supplier<BlockEntityType<TEStoneCrucible>> teStoneCrucible;
	public static Supplier<BlockEntityType<TEIronWorkbench>> teIronWorkbench;
	public static Supplier<BlockEntityType<TEStoneWorkbench>> teStoneWorkbench;
	public static Supplier<BlockEntityType<TileEntitySolarPanel>> teSolarPanel;
	public static Supplier<BlockEntityType<TileEntityCable>> teCable;
	public static Supplier<BlockEntityType<TEScrewPump>> teScrewPump;
	public static Supplier<BlockEntityType<TEWoodenTank>> teWoodenTank;
	public static Supplier<BlockEntityType<TESplitter>> teSplitter;
	public static Supplier<BlockEntityType<TETumblingBarrel>> teTumblingBarrel;
	public static Supplier<BlockEntityType<TEBrickCastingVessel>> teBrickCastingVessel;
	public static Supplier<BlockEntityType<TEJoiner>> teJoiner;
	public static Supplier<BlockEntityType<TEBrickMaker>> teBrickMaker;

	static
	{
		// FABlocks.Init();
		// RegisterTileEntities();
	}

	public static void RegisterTileEntities()
	{
		if (!tileTypes.isEmpty())
			return;

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
		teBevelGear = BuildType(TEBevelGear::new, FABlocks.bevelGears, "bevel_gear");
		teGearbox = BuildType(TEGearbox::new, FABlocks.gearboxes, "gearbox");
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
		teSplitter = BuildType(TESplitter::new, FABlocks.splitters, "splitter");
		teTumblingBarrel = BuildType(TETumblingBarrel::new, FABlocks.tumblingBarrel, "tumbling_barrel");
		teBrickCastingVessel = BuildType(TEBrickCastingVessel::new, FABlocks.brickCastingVessel, "brick_casting_vessel");
		teJoiner = BuildType(TEJoiner::new, FABlocks.joiners, "joiner");
		teBrickMaker = BuildType(TEBrickMaker::new, FABlocks.brickMakerFrame, "brick_maker");
	}

	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> BuildType(
			BlockEntityType.BlockEntitySupplier<T> supplier,
			Block block, String name)
	{
		if (block == null)
			System.err.println("how wtf");
		var loc = new ResourceLocation(MODID, name);
		var type = RegistryObjectWrapper.Of(name, BlockEntityType.Builder.of(supplier, block).build(null));
		tileTypes.add((RegistryObjectWrapper) type);
		return type::obj;
		// return TE_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, block).build(null));
	}

	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> BuildType(
			BlockEntityType.BlockEntitySupplier<T> supplier,
			List<Block> blocks, String name)
	{
		var loc = new ResourceLocation(MODID, name);
		var type = RegistryObjectWrapper.Of(name, BlockEntityType.Builder.of(supplier, blocks.toArray(Block[]::new)).build(null));
		tileTypes.add((RegistryObjectWrapper) type);
		return type::obj;
		// return TE_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.toArray(Block[]::new)).build(null));
	}

	@SubscribeEvent
	public static void RegisterTypes(RegisterEvent event)
	{
		RegisterTileEntities();
		event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, n -> tileTypes.forEach(t -> t.Register(n)));
	}
}
