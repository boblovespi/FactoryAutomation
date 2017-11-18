package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Willi on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class TileEntityHandler
{

	public static void RegisterTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityBlastFurnaceController.class,
				FactoryAutomation.MODID + ":tile_entity_blast_furnace");
	}
}
