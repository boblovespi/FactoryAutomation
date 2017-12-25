package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import boblovespi.factoryautomation.common.tileentity.TileEntityMultiblockPart;
import boblovespi.factoryautomation.common.tileentity.electricity.TileEntitySolarPanel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class TileEntityHandler
{

	public static void RegisterTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityBlastFurnaceController.class,
										MODID + ":tile_entity_blast_furnace");
		GameRegistry.registerTileEntity(TileEntityMultiblockPart.class,
										MODID + ":tile_entity_multiblock_part");
		GameRegistry.registerTileEntity(TileEntitySolarPanel.class,
										MODID + ":tile_entity_solar_panel");
		GameRegistry.registerTileEntity(TESteelmakingFurnace.class, MODID
				+ ":tile_entity_steelmaking_furnace");
	}
}
