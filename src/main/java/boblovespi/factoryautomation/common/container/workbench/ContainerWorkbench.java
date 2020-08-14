package boblovespi.factoryautomation.common.container.workbench;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.tileentity.workbench.TEStoneWorkbench;
import boblovespi.factoryautomation.common.tileentity.workbench.TEWorkbench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.container.Container;
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
			for (int x1 = 0; x1 < 5; x1++)
			{
				for (int y1 = 0; y1 < 3; y1++)
				{
					addSlotToContainer(new SlotItemHandler(inv, y1 + x1 * 3 + 2,
							16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 44 + (x1 - 2) * 18)), 35 + 18 * y1));
				}
			}
		} else
		{
			is3x3 = false;
			for (int x1 = 0; x1 < 7; x1++)
			{
				for (int y1 = 0; y1 < 5; y1++)
				{
					addSlotToContainer(new SlotItemHandler(inv, y1 + x1 * 5 + 2,
							16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 26 + (x1 - 2) * 18)), 17 + 18 * y1));
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
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return !playerIn.isSpectator();
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int fromSlot)
	{
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(fromSlot);

		if (slot != null && slot.getHasStack())
		{
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < this.inv.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.mergeItemStack(current, inv.getSlots(), inv.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.mergeItemStack(current, 1, inv.getSlots(), false))
					return ItemStack.EMPTY;
			}

			if (current.isEmpty()) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
				slot.putStack(
						ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else
				slot.onSlotChanged();

			if (current.getCount() == previous.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, current);

		}
		return previous;
	}

}
