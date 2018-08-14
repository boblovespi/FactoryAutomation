package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEPowerShaft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class TileEntityHandler
{
	public static List<Class<? extends TileEntity>> tiles = new ArrayList<>(10);

	public static void RegisterTileEntities()
	{
		GameRegistry.registerTileEntity(TEBlastFurnaceController.class,
				new ResourceLocation(MODID, "tile_entity_blast_furnace"));
		GameRegistry
				.registerTileEntity(TEMultiblockPart.class, new ResourceLocation(MODID, "tile_entity_multiblock_part"));
		GameRegistry
				.registerTileEntity(TileEntitySolarPanel.class, new ResourceLocation(MODID, "tile_entity_solar_panel"));
		GameRegistry.registerTileEntity(TESteelmakingFurnace.class,
				new ResourceLocation(MODID, "tile_entity_steelmaking_furnace"));
		GameRegistry.registerTileEntity(TEPowerShaft.class, new ResourceLocation(MODID, "tile_entity_power_shaft"));

		tiles.forEach(n -> GameRegistry.registerTileEntity(n,
				new ResourceLocation(MODID, "tile_entity_" + n.getName().substring(n.getName().lastIndexOf(".") + 1))));
	}
}
