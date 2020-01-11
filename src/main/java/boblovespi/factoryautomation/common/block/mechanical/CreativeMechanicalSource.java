package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TECreativeMechanicalSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Created by Willi on 2/20/2018.
 */
public class CreativeMechanicalSource extends FABaseBlock
{
	public CreativeMechanicalSource()
	{
		super(Material.MISCELLANEOUS, "creative_mechanical_source", null);
		TileEntityHandler.tiles.add(TECreativeMechanicalSource.class);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TECreativeMechanicalSource();
	}
}
