package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
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
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityHandler
{
	public static List<Class<? extends BlockEntity>> tiles = new ArrayList<>(32);
	public static List<BlockEntityType<?>> tileTypes = new ArrayList<>(32);
	public static BlockEntityType<TECreativeMechanicalSource> teCreativeMechanicalSource;
	public static BlockEntityType<TEMultiblockPart> teMultiblockPart;
	public static BlockEntityType<TESolidFueledFirebox> teSolidFueledFirebox;
	public static BlockEntityType<TEHorseEngine> teHorseEngine;
	public static BlockEntityType<TEBasicCircuitCreator> teBasicCircuitCreator;
	public static BlockEntityType<TEBlastFurnaceController> teBlastFurnaceController;
	public static BlockEntityType<TESteelmakingFurnace> teSteelmakingFurnace;
	public static BlockEntityType<TEPipe> tePipe;
	public static BlockEntityType<TEPlacedBucket> tePlacedBucket;
	public static BlockEntityType<TEPump> tePump;
	public static BlockEntityType<TEBevelGear> teBevelGear;
	public static BlockEntityType<TEGearbox> teGearbox;
	public static BlockEntityType<TEHandCrank> teHandCrank;
	public static BlockEntityType<TEJawCrusher> teJawCrusher;
	public static BlockEntityType<TEMillstone> teMillstone;
	public static BlockEntityType<TELeatherBellows> teLeatherBellows;
	public static BlockEntityType<TEMotor> teMotor;
	public static BlockEntityType<TEPowerShaft> tePowerShaft;
	public static BlockEntityType<TETripHammerController> teTripHammerController;
	public static BlockEntityType<TEWaterwheel> teWaterwheel;
	public static BlockEntityType<TECampfire> teCampfire;
	public static BlockEntityType<TEChoppingBlock> teChoppingBlock;
	public static BlockEntityType<TETreetap> teTreetap;
	public static BlockEntityType<TEBrickCrucible> teBrickCrucible;
	public static BlockEntityType<TEPaperBellows> tePaperBellows;
	public static BlockEntityType<TEStoneCastingVessel> teStoneCastingVessel;
	public static BlockEntityType<TEStoneCrucible> teStoneCrucible;
	public static BlockEntityType<TEIronWorkbench> teIronWorkbench;
	public static BlockEntityType<TEStoneWorkbench> teStoneWorkbench;
	public static BlockEntityType<TileEntitySolarPanel> teSolarPanel;
	public static BlockEntityType<TileEntityCable> teCable;
	public static BlockEntityType<TEScrewPump> teScrewPump;
	public static BlockEntityType<TEWoodenTank> teWoodenTank;

	public static void RegisterTileEntities()
	{
		teCreativeMechanicalSource = BuildType(TECreativeMechanicalSource::new, FABlocks.creativeMechanicalSource, "creative_mechanical_source");
		teMultiblockPart = BuildType(TEMultiblockPart::new, FABlocks.multiblockPart, "multiblock_part");
		teSolidFueledFirebox = BuildType(TESolidFueledFirebox::new, FABlocks.solidfueledfirebox, "solid_fueled_firebox");
		teHorseEngine = BuildType(TEHorseEngine::new, FABlocks.horseEngine, "horse_engine");
		teBasicCircuitCreator = BuildType(TEBasicCircuitCreator::new, FABlocks.chipCreator, "basic_circuit_creator");
		teBlastFurnaceController = BuildType(TEBlastFurnaceController::new, FABlocks.blastFurnaceController, "blast_furnace");
		teSteelmakingFurnace = BuildType(TESteelmakingFurnace::new, FABlocks.steelmakingFurnaceController, "steelmaking_furnace");
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
		teStoneCrucible = BuildType(TEStoneCrucible::new, FABlocks.stoneCrucible, "stone_crucible");
		teIronWorkbench = BuildType(TEIronWorkbench::new, FABlocks.ironWorkbench, "iron_workbench");
		teStoneWorkbench = BuildType(TEStoneWorkbench::new, FABlocks.stoneWorkbench, "stone_workbench");
		teSolarPanel = BuildType(TileEntitySolarPanel::new, FABlocks.solarPanel, "solar_panel");
		teCable = BuildType(TileEntityCable::new, FABlocks.cable, "cable");
		teScrewPump = BuildType(TEScrewPump::new, FABlocks.screwPump, "screw_pump");
		teWoodenTank = BuildType(TEWoodenTank::new, FABlocks.woodenTank, "wooden_tank");
	}

	private static <T extends BlockEntity> BlockEntityType<T> BuildType(BlockEntityType.BlockEntitySupplier<T> supplier,
																		FABlock block, String name)
	{
		ResourceLocation loc = new ResourceLocation(MODID, name);
		BlockEntityType<T> t = BlockEntityType.Builder.of(supplier, block.ToBlock())
				.build(Util.fetchChoiceType(References.BLOCK_ENTITY, loc.toString()));
		t.setRegistryName(loc);
		tileTypes.add(t);
		return t;
	}

	private static <T extends BlockEntity> BlockEntityType<T> BuildType(BlockEntityType.BlockEntitySupplier<T> supplier,
																		List<FABlock> blocks, String name)
	{
		ResourceLocation loc = new ResourceLocation(MODID, name);
		BlockEntityType<T> t = BlockEntityType.Builder.of(supplier, blocks.stream().map(FABlock::ToBlock)
				.toArray(Block[]::new)).build(Util.fetchChoiceType(References.BLOCK_ENTITY, loc.toString()));
		t.setRegistryName(loc);
		tileTypes.add(t);
		return t;
	}

	@SubscribeEvent
	public static void RegisterTypes(RegistryEvent.Register<BlockEntityType<?>> event)
	{
		RegisterTileEntities();
		tileTypes.forEach(event.getRegistry()::register);
	}
}
