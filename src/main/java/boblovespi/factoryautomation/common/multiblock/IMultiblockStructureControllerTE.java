package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 11/19/2017.
 */
public interface IMultiblockStructureControllerTE
{
	default void SetStructureValid()
	{
		SetStructureValid(true);
	}

	void SetStructureValid(boolean isValid);

	default void SetStructureInvalid()
	{
		SetStructureValid(false);
	}

	boolean IsStructureValid();

	void CreateStructure(World world, BlockPos pos);

	void BreakStructure(World world, BlockPos pos);
}
