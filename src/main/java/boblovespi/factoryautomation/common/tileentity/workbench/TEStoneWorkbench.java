package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class TEStoneWorkbench extends TEWorkbench
{
	public TEStoneWorkbench(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teIronWorkbench, 3, 1, pos, state);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerStoneWorkbench(id, playerInv, inventory, worldPosition);
	}
}
