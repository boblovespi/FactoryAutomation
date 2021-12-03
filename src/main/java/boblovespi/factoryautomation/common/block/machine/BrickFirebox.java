package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.TESolidFueledFirebox;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 10/28/2018.
 */
public class BrickFirebox extends FABaseBlock /*implements ITileEntityProvider*/
{
	public BrickFirebox()
	{
		super(Material.METAL, "brick_firebox", FAItemGroups.heat);
		TileEntityHandler.tiles.add(TESolidFueledFirebox.class);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}
}
