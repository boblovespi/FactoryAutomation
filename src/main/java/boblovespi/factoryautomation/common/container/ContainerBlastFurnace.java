package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TileEntityBlastFurnaceController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;

/**
 * Created by Willi on 11/12/2017.
 */

/**
 * Created by Willi on 4/13/2017.
 */
public class ContainerBlastFurnace extends Container
{
	private TileEntityBlastFurnaceController te;
	private IItemHandler handler;

	public ContainerBlastFurnace(IInventory playerInv, TileEntity te)
	{
		handler = te
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
						null);

		this.te = (TileEntityBlastFurnaceController) te;

		addSlotToContainer(new SlotItemHandler(handler,
				TileEntityBlastFurnaceController.TUYERE_SLOT, 8, 8));
		addSlotToContainer(new SlotRestrictedItem(handler,
				TileEntityBlastFurnaceController.IRON_SLOT, 47, 17,
				Arrays.asList(Items.IRON_INGOT)));
		addSlotToContainer(new SlotRestrictedItem(handler,
				TileEntityBlastFurnaceController.FLUX_SLOT, 65, 17,
				Arrays.asList(Items.REDSTONE)));
		addSlotToContainer(new SlotRestrictedItem(handler,
				TileEntityBlastFurnaceController.COKE_SLOTS[0], 47, 53,
				Arrays.asList(FAItems.coalCoke.ToItem())));
		addSlotToContainer(new SlotRestrictedItem(handler,
				TileEntityBlastFurnaceController.COKE_SLOTS[1], 65, 53,
				Arrays.asList(FAItems.coalCoke.ToItem())));
		addSlotToContainer(new SlotOutputItem(handler,
				TileEntityBlastFurnaceController.OUTPUT_SLOT, 116, 35));
		addSlotToContainer(new SlotOutputItem(handler,
				TileEntityBlastFurnaceController.SLAG_SLOT, 142, 35));

		int x = 8;
		int y = 84;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlotToContainer(
						new Slot(playerInv, i + j * 9 + 9, x + i * 18,
								y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(playerInv, i, x + i * 18, 142));
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

			if (fromSlot < this.handler.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.mergeItemStack(current, handler.getSlots(),
						handler.getSlots() + 36, true))
					return null;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
					return null;
			}

			if (current.getCount()
					== 0) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
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

