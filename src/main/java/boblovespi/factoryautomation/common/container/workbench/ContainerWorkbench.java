package boblovespi.factoryautomation.common.container.workbench;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Willi on 4/9/2018.
 */
public class ContainerWorkbench extends Container
{
	public final boolean is3x3;
	protected TEWorkbench te;
	protected IItemHandler inv;

	public ContainerWorkbench(IInventory playerInv, TileEntity bench)
	{
		te = (TEWorkbench) bench;
		inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		if (te instanceof TEStoneWorkbench)
		{
			is3x3 = true;
			for (int i = 0; i < 5; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					addSlotToContainer(new SlotItemHandler(inv, j + i * 3 + 2,
							16 + (i < 1 ? 0 : 26 + (i < 2 ? 0 : 44 + (i - 2) * 18)), 35 + 18 * j));
				}
			}
		} else
		{
			is3x3 = false;
			for (int i = 0; i < 7; i++)
			{
				for (int j = 0; j < 5; j++)
				{
					addSlotToContainer(new SlotItemHandler(inv, j + i * 3 + 2,
							15 + (i < 1 ? 0 : 26 + (i < 2 ? 0 : 44 + (i - 2) * 18)), 16 + 18 * j));
				}
			}
		}

		addSlotToContainer(new SlotOutputItem(inv, 0, 198, 53));
		addSlotToContainer(new SlotItemHandler(inv, 1, 198, 89));

		int x = 37;
		int y = 120;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlotToContainer(new Slot(playerInv, i + j * 9 + 9, x + i * 18, y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(playerInv, i, x + i * 18, y + 58));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !playerIn.isSpectator();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot)
	{
		ItemStack previous = null;
		Slot slot = this.inventorySlots.get(fromSlot);

		if (slot != null && slot.getHasStack())
		{
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < this.inv.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.mergeItemStack(current, inv.getSlots(), inv.getSlots() + 36, true))
					return null;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.mergeItemStack(current, 0, inv.getSlots(), false))
					return null;
			}

			if (current.getCount() == 0) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
				slot.putStack(
						ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else
				slot.onSlotChanged();

			if (current.getCount() == previous.getCount())
				return null;
			slot.onTake(playerIn, current);

		}
		return previous;
	}
}
