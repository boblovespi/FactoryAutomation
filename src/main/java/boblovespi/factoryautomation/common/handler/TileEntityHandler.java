package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.tileentity.*;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TECreativeMechanicalSource;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEHorseEngine;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber
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

		GameRegistry.registerTileEntity(TEBlastFurnaceController.class,
				new ResourceLocation(MODID, "tile_entity_blast_furnace"));
		GameRegistry
				.registerTileEntity(TEMultiblockPart.class, new ResourceLocation(MODID, "tile_entity_multiblock_part"));
		GameRegistry
				.registerTileEntity(TileEntitySolarPanel.class, new ResourceLocation(MODID, "tile_entity_solar_panel"));
		GameRegistry.registerTileEntity(TESteelmakingFurnace.class,
				new ResourceLocation(MODID, "tile_entity_steelmaking_furnace"));
		GameRegistry.registerTileEntity(TEPowerShaft.class, new ResourceLocation(MODID, "tile_entity_power_shaft"));
		GameRegistry
				.registerTileEntity(TEChoppingBlock.class, new ResourceLocation(MODID, "tile_entity_chopping_block"));

		tiles.forEach(n -> GameRegistry.registerTileEntity(n,
				new ResourceLocation(MODID, "tile_entity_" + n.getName().substring(n.getName().lastIndexOf(".") + 1))));
	}

	private static <T extends TileEntity> TileEntityType<T> BuildType(Supplier<T> supplier, FABlock block, String name)
	{
		TileEntityType<T> t = TileEntityType.Builder.create(supplier, block.ToBlock()).build(null);
		t.setRegistryName(new ResourceLocation(FactoryAutomation.MODID, name));
		return t;
	}
}
