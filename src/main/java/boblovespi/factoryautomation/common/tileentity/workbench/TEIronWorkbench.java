package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/15/2018.
 */
public class TEIronWorkbench extends TEWorkbench
{
	public TEIronWorkbench(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teIronWorkbench.get(), 5, 2, pos, state);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerIronWorkbench(id, playerInv, inventory, worldPosition);
	}
}
