package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 11/17/2017.
 *
 * @deprecated Use IMultiblockControllerTE
 */

@Deprecated
public interface IMultiblockStructureController
{
	String GetPatternId();

	default boolean IsValidStructure(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}

	default void CreateStructure(World world, BlockPos pos)
	{
	}

	default void BreakStructure(World world, BlockPos pos)
	{
	}

	default void SetStructureCompleted(World world, BlockPos pos, boolean completed)
	{
	}
}
