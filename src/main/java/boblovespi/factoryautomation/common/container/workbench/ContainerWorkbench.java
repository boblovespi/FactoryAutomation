package boblovespi.factoryautomation.common.container.workbench;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 4/9/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ContainerWorkbench extends AbstractContainerMenu
{
	public final boolean is3x3;
	protected IItemHandler inv;
	protected Player player;

	public ContainerWorkbench(int id, Inventory playerInv, IItemHandler inv, BlockPos pos, boolean is3x3,
			MenuType<?> type)
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
	public boolean stillValid(Player playerIn)
	{
		return !playerIn.isSpectator();
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int fromSlot)
	{
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.slots.get(fromSlot);

		if (slot != null && slot.hasItem())
		{
			ItemStack current = slot.getItem();
			previous = current.copy();

			if (fromSlot < this.inv.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.moveItemStackTo(current, inv.getSlots(), inv.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.moveItemStackTo(current, 1, inv.getSlots(), false))
					return ItemStack.EMPTY;
			}

			if (current.isEmpty()) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
				slot.set(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else
				slot.setChanged();

			if (current.getCount() == previous.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, current);

		}
		return previous;
	}

	@Override
	public void broadcastChanges()
	{
		boolean shouldResendOutput = false;
		boolean hasResentOutput = false;
		for (int i = 0; i < this.slots.size(); ++i)
		{
			ItemStack itemstack = this.slots.get(i).getItem();
			ItemStack itemstack1 = this.lastSlots.get(i);
			if (!ItemStack.matches(itemstack1, itemstack))
			{
				boolean clientStackChanged = !itemstack1.equals(itemstack, true);
				shouldResendOutput = shouldResendOutput || (clientStackChanged && i != 0);
				hasResentOutput = hasResentOutput || (clientStackChanged && i == 0);
				itemstack1 = itemstack.copy();
				this.lastSlots.set(i, itemstack1);

				if (clientStackChanged)
					for (ContainerListener listener : this.containerListeners)
					{
						if (listener instanceof ServerPlayer)
							((ServerPlayer) listener).connection
									.send(new ClientboundContainerSetSlotPacket(containerId, i, itemstack1));
						else
							listener.slotChanged(this, i, itemstack1);
					}
			}
		}
		if (shouldResendOutput && !hasResentOutput)
			for (ContainerListener listener : this.containerListeners)
			{
				if (listener instanceof ServerPlayer)
					((ServerPlayer) listener).connection
							.send(new ClientboundContainerSetSlotPacket(containerId, 0, slots.get(0).getItem()));
				else
					listener.slotChanged(this, 0, slots.get(0).getItem());
			}

		for (int j = 0; j < this.dataSlots.size(); ++j)
		{
			DataSlot intreferenceholder = this.dataSlots.get(j);
			if (intreferenceholder.checkAndClearUpdateFlag())
			{
				for (ContainerListener containerListener : this.containerListeners)
				{
					containerListener.setContainerData(this, j, intreferenceholder.get());
				}
			}
		}
	}
}