package boblovespi.factoryautomation.common.block.workbench;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class StoneWorkbench extends FABaseBlock implements ITileEntityProvider
{
	public StoneWorkbench()
	{
		super(Material.ROCK, "stone_workbench");
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
		return new TEStoneWorkbench();
	}
}
