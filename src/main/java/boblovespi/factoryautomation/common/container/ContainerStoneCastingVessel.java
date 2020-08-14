package boblovespi.factoryautomation.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Willi on 12/30/2018.
 */
public class ContainerStoneCastingVessel extends Container
{
	private TileEntity te;

	public ContainerStoneCastingVessel(IInventory playerInv, TileEntity te)
	{
		this.te = te;
		int x = 8;
		int y = 98;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlot(new Slot(playerInv, i + j * 9 + 9, x + i * 18, y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(playerInv, i, x + i * 18, y + 58));
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return !playerIn.isSpectator();
	}

	public BlockPos GetPos()
	{
		return te.getPos();
	}
}
