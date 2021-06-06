package boblovespi.factoryautomation.api.energy.electricity;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created by Willi on 12/21/2017.
 */
public interface IEnergyBlock
{
	/**
	 * Checks whether or not a cable can connect to the given side and state
	 *
	 * @param state The state of the machine
	 * @param side  The side power is being connected to
	 * @param level The level access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	boolean canConnectCable(BlockState state, Direction side,
							IBlockReader level, BlockPos pos);
}
