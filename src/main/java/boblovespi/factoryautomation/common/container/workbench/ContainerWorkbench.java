package boblovespi.factoryautomation.common.container.workbench;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Willi on 4/9/2018.
 */
public abstract class ContainerWorkbench extends Container
{
	public final boolean is3x3;
	protected IItemHandler inv;
	protected PlayerEntity player;

	public ContainerWorkbench(int id, PlayerInventory playerInv, IItemHandler inv, BlockPos pos, boolean is3x3,
			ContainerType<?> type)
	{
		super(type, id);
		this.inv = inv;
		player = playerInv.player;

		addSlot(new SlotOutputItem(inv, 0, 198, 53));
		addSlot(new SlotItemHandler(inv, 1, 198, 89));

		if (is3x3)
		{
			this.is3x3 = true;
			for (int x1 = 0; x1 < 5; x1++)
			{
				for (int y1 = 0; y1 < 3; y1++)
				{
					addSlot(new SlotItemHandler(inv, y1 + x1 * 3 + 2,
							16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 44 + (x1 - 2) * 18)), 35 + 18 * y1));
				}
			}
		} else
		{
			this.is3x3 = false;
			for (int x1 = 0; x1 < 7; x1++)
			{
				for (int y1 = 0; y1 < 5; y1++)
				{
					addSlot(new SlotItemHandler(inv, y1 + x1 * 5 + 2,
							16 + (x1 < 1 ? 0 : 26 + (x1 < 2 ? 0 : 26 + (x1 - 2) * 18)), 17 + 18 * y1));
				}
			}
		}

		int x = 37;
		int y = 120;

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

	@Override
	public void detectAndSendChanges()
	{
		boolean shouldResendOutput = false;
		boolean hasResentOutput = false;
		for (int i = 0; i < this.inventorySlots.size(); ++i)
		{
			ItemStack itemstack = this.inventorySlots.get(i).getStack();
			ItemStack itemstack1 = this.inventoryItemStacks.get(i);
			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
			{
				boolean clientStackChanged = !itemstack1.equals(itemstack, true);
				shouldResendOutput = shouldResendOutput || (clientStackChanged && i != 0);
				hasResentOutput = hasResentOutput || (clientStackChanged && i == 0);
				itemstack1 = itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);

				if (clientStackChanged)
					for (IContainerListener listener : this.listeners)
					{
						if (listener instanceof ServerPlayerEntity)
							((ServerPlayerEntity) listener).connection
									.sendPacket(new SSetSlotPacket(windowId, i, itemstack1));
						else
							listener.sendSlotContents(this, i, itemstack1);
					}
			}
		}
		if (shouldResendOutput && !hasResentOutput)
			for (IContainerListener listener : this.listeners)
			{
				if (listener instanceof ServerPlayerEntity)
					((ServerPlayerEntity) listener).connection
							.sendPacket(new SSetSlotPacket(windowId, 0, inventorySlots.get(0).getStack()));
				else
					listener.sendSlotContents(this, 0, inventorySlots.get(0).getStack());
			}

		for (int j = 0; j < this.trackedIntReferences.size(); ++j)
		{
			IntReferenceHolder intreferenceholder = this.trackedIntReferences.get(j);
			if (intreferenceholder.isDirty())
			{
				for (IContainerListener containerListener : this.listeners)
				{
					containerListener.sendWindowProperty(this, j, intreferenceholder.get());
				}
			}
		}
	}
}