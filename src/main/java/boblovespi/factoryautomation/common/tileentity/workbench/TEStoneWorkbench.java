package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/8/2018.
 */
public class TEStoneWorkbench extends TEWorkbench
{
	public TEStoneWorkbench()
	{
		super(TileEntityHandler.teStoneWorkbench, 3, 1);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerStoneWorkbench(id, playerInv, inventory, worldPosition);
	}
}
