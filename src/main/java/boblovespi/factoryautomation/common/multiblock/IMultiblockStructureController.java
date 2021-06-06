package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.block.BlockState;
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
	String getPatternId();

	default boolean isValidStructure(World world, BlockPos pos, BlockState state)
	{
		return false;
	}

	default void createStructure(World world, BlockPos pos)
	{
	}

	default void breakStructure(World world, BlockPos pos)
	{
	}

	default void setStructureCompleted(World world, BlockPos pos, boolean completed)
	{
	}
}
