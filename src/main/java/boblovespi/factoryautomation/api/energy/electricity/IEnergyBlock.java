package boblovespi.factoryautomation.api.energy.electricity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

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
	boolean CanConnectCable(BlockState state, Direction side,
			BlockGetter level, BlockPos pos);
}
