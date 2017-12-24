package boblovespi.factoryautomation.common.block.machine;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.multiblock.IMultiblockStructureController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/23/2017.
 */
public class SteelmakingFurnaceController extends FABaseBlock implements
		ITileEntityProvider, IMultiblockStructureController
{
	public SteelmakingFurnaceController()
	{
		super(Material.DRAGON_EGG, "steelmaking_furnace_controller");
	}

	@Override
	public String GetPatternId()
	{
		return "steelmaking_furnace";
	}

	@Override
	public void CreateStructure(World world, BlockPos pos)
	{

	}

	@Override
	public void BreakStructure(World world, BlockPos pos)
	{

	}

	@Override
	public void SetStructureCompleted(World world, BlockPos pos,
			boolean completed)
	{

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
		return null;
	}
}
