package boblovespi.factoryautomation.api.energy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

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
	 * @param world The world access
	 * @param pos   The position of the block
	 * @return Whether or not a cable can attach to the given side and state
	 */
	boolean CanConnectCable(IBlockState state, EnumFacing side,
			IBlockAccess world, BlockPos pos);
}
