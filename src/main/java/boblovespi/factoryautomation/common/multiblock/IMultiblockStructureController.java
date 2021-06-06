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

	default boolean isValidStructure(World level, BlockPos pos, BlockState state)
	{
		return false;
	}

	default void createStructure(World level, BlockPos pos)
	{
	}

	default void breakStructure(World level, BlockPos pos)
	{
	}

	default void setStructureCompleted(World level, BlockPos pos, boolean completed)
	{
	}
}
