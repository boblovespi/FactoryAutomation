package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Created by Willi on 11/17/2017.
 *
 * @deprecated Use IMultiblockControllerTE
 */

@Deprecated
public interface IMultiblockStructureController
{
	String GetPatternId();

	default boolean IsValidStructure(Level level, BlockPos pos, BlockState state)
	{
		return false;
	}

	default void CreateStructure(Level level, BlockPos pos)
	{
	}

	default void BreakStructure(Level level, BlockPos pos)
	{
	}

	default void SetStructureCompleted(Level level, BlockPos pos, boolean completed)
	{
	}
}
