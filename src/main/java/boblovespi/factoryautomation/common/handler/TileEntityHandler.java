package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.*;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntityCable;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.tileentity.mechanical.*;
import boblovespi.factoryautomation.common.tileentity.processing.TECampfire;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import boblovespi.factoryautomation.common.tileentity.processing.TETreetap;
import boblovespi.factoryautomation.common.tileentity.smelting.TEBrickCrucible;
import boblovespi.factoryautomation.common.tileentity.smelting.TEPaperBellows;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCrucible;
import boblovespi.factoryautomation.common.tileentity.workbench.TEIronWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class TileEntityHandler
{
	public static List<Class<? extends TileEntity>> tiles = new ArrayList<>(10);
	public static List<TileEntityType<?>> tileTypes = new ArrayList<>(20);
	public static TileEntityType<TECreativeMechanicalSource> teCreativeMechanicalSource;
	public static TileEntityType<TEMultiblockPart> teMultiblockPart;
	public static TileEntityType<TESolidFueledFirebox> teSolidFueledFirebox;
	public static TileEntityType<TEHorseEngine> teHorseEngine;
	public static TileEntityType<TEBasicCircuitCreator> teBasicCircuitCreator;
	public static TileEntityType<TEBlastFurnaceController> teBlastFurnaceController;
	public static TileEntityType<TESteelmakingFurnace> teSteelmakingFurnace;
	public static TileEntityType<TEPipe> tePipe;
	public static TileEntityType<TEPlacedBucket> tePlacedBucket;
	public static TileEntityType<TEPump> tePump;
	public static TileEntityType<TEBevelGear> teBevelGear;
	public static TileEntityType<TEGearbox> teGearbox;
	public static TileEntityType<TEHandCrank> teHandCrank;
	public static TileEntityType<TEJawCrusher> teJawCrusher;
	public static TileEntityType<TEMillstone> teMillstone;
	public static TileEntityType<TELeatherBellows> teLeatherBellows;
	public static TileEntityType<TEMotor> teMotor;
	public static TileEntityType<TEPowerShaft> tePowerShaft;
	public static TileEntityType<TETripHammerController> teTripHammerController;
	public static TileEntityType<TEWaterwheel> teWaterwheel;
	public static TileEntityType<TECampfire> teCampfire;
	public static TileEntityType<TEChoppingBlock> teChoppingBlock;
	public static TileEntityType<TETreetap> teTreetap;
	public static TileEntityType<TEBrickCrucible> teBrickCrucible;
	public static TileEntityType<TEPaperBellows> tePaperBellows;
	public static TileEntityType<TEStoneCastingVessel> teStoneCastingVessel;
	public static TileEntityType<TEStoneCrucible> teStoneCrucible;
	public static TileEntityType<TEIronWorkbench> teIronWorkbench;
	public static TileEntityType<TEStoneWorkbench> teStoneWorkbench;
	public static TileEntityType<TileEntitySolarPanel> teSolarPanel;
	public static TileEntityType<TileEntityCable> teCable;

	public static void RegisterTileEntities()
	{
		teCreativeMechanicalSource = BuildType(TECreativeMechanicalSource::new, FABlocks.creativeMechanicalSource,
				"creative_mechanical_source");
		teMultiblockPart = BuildType(TEMultiblockPart::new, FABlocks.multiblockPart, "multiblock_part");
		teHorseEngine = BuildType(TEHorseEngine::new, FABlocks.horseEngine, "horse_engine");
		teBasicCircuitCreator = BuildType(TEBasicCircuitCreator::new, FABlocks.chipCreator, "basic_circuit_creator");
		teBlastFurnaceController = BuildType(
				TEBlastFurnaceController::new, FABlocks.blastFurnaceController, "blast_furnace");
		teSteelmakingFurnace = BuildType(
				TESteelmakingFurnace::new, FABlocks.steelmakingFurnaceController, "steelmaking_furnace");
		tePipe = BuildType(TEPipe::new, FABlocks.pipe, "pipe");
		tePlacedBucket = BuildType(TEPlacedBucket::new, FABlocks.placedBucket, "placed_bucket");
		tePump = BuildType(TEPump::new, FABlocks.pump, "pump");
		teBevelGear = BuildType(TEBevelGear::new, FABlocks.bevelGear, "bevel_gear");
		teGearbox = BuildType(TEGearbox::new, FABlocks.gearbox, "gearbox");
		teHandCrank = BuildType(TEHandCrank::new, FABlocks.handCrank, "hand_crank");
		teJawCrusher = BuildType(TEJawCrusher::new, FABlocks.jawCrusher, "jaw_crusher");
		teMillstone = BuildType(TEMillstone::new, FABlocks.millstone, "millstone");
		teLeatherBellows = BuildType(TELeatherBellows::new, FABlocks.leatherBellows, "leather_bellows");
		teMotor = BuildType(TEMotor::new, FABlocks.motor, "motor");
		tePowerShaft = BuildType(TEPowerShaft::new, FABlocks.powerShaft, "power_shaft");
		teTripHammerController = BuildType(TETripHammerController::new, FABlocks.tripHammerController, "trip_hammer");
		teWaterwheel = BuildType(TEWaterwheel::new, FABlocks.waterwheel, "waterwheel");
		teCampfire = BuildType(TECampfire::new, FABlocks.campfire, "campfire");
		teChoppingBlock = BuildType(TEChoppingBlock::new, FABlocks.woodChoppingBlocks, "chopping_block");
		teTreetap = BuildType(TETreetap::new, FABlocks.treetap, "treetap");
		teBrickCrucible = BuildType(TEBrickCrucible::new, FABlocks.brickCrucible, "brick_crucible");
		tePaperBellows = BuildType(TEPaperBellows::new, FABlocks.paperBellows, "paper_bellows");
		teStoneCastingVessel = BuildType(TEStoneCastingVessel::new, FABlocks.stoneCastingVessel, "stone_casting_vessel");
		teIronWorkbench = BuildType(TEIronWorkbench::new, FABlocks.ironWorkbench, "iron_workbench");
		teStoneWorkbench = BuildType(TEStoneWorkbench::new, FABlocks.stoneWorkbench, "stone_workbench");
		teSolarPanel = BuildType(TileEntitySolarPanel::new, FABlocks.solarPanel, "solar_panel");
		teCable = BuildType(TileEntityCable::new, FABlocks.cable, "cable");
	}

	private static <T extends TileEntity> TileEntityType<T> BuildType(Supplier<T> supplier, FABlock block, String name)
	{
		TileEntityType<T> t = TileEntityType.Builder.create(supplier, block.ToBlock()).build(null);
		t.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, name));
		return t;
	}

	private static <T extends TileEntity> TileEntityType<T> BuildType(Supplier<T> supplier, List<FABlock> blocks,
			String name)
	{
		TileEntityType<T> t = TileEntityType.Builder
				.create(supplier, blocks.stream().map(FABlock::ToBlock).toArray(Block[]::new)).build(null);
		t.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, name));
		return t;
	}
}
