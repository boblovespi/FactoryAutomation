package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickable
{
	static void tickTE(Level world, BlockPos pos, BlockState state, BlockEntity te)
	{
		if (te instanceof ITickable tile)
			tile.tick();
	}

	void tick();
}
