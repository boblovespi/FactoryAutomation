package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.common.container.workbench.ContainerIronWorkbench;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;

/**
 * Created by Willi on 4/15/2018.
 */
public class TEIronWorkbench extends TEWorkbench
{
	public TEIronWorkbench()
	{
		super(TileEntityHandler.teIronWorkbench, 5, 2);
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerIronWorkbench(id, playerInv, inventory, worldPosition);
	}
}
