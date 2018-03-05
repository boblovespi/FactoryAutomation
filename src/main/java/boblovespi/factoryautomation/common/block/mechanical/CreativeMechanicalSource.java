package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TECreativeMechanicalSource;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/20/2018.
 */
public class CreativeMechanicalSource extends FABaseBlock
		implements ITileEntityProvider
{
	public CreativeMechanicalSource()
	{
		super(Material.CIRCUITS, "creative_mechanical_source");
		TileEntityHandler.tiles.add(TECreativeMechanicalSource.class);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 *
	 * @param worldIn
	 * @param meta
	 */
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TECreativeMechanicalSource();
	}

}
