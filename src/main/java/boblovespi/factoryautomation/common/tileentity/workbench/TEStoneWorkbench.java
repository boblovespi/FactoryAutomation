package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.common.container.workbench.ContainerStoneWorkbench;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

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
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerStoneWorkbench(id, playerInv, inventory, pos);
	}
}
