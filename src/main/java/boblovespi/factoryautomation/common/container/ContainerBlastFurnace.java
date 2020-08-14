package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Collections;

/**
 * Created by Willi on 11/12/2017.
 */

/**
 * Created by Willi on 4/13/2017.
 */
public class ContainerBlastFurnace extends Container
{
	public static final ContainerType<ContainerBlastFurnace> TYPE = IForgeContainerType.create(ContainerBlastFurnace::new);
	private TEBlastFurnaceController te;
	private IItemHandler handler;
	private IIntArray progressBars;

	// server-side container
	public ContainerBlastFurnace(int id, PlayerInventory playerInv, IItemHandler inv, IIntArray progressBars, BlockPos pos)
	{
		super(TYPE, id);
		handler = inv;
		this.progressBars = progressBars;
		trackIntArray(progressBars);

		addSlot(new SlotItemHandler(handler, TEBlastFurnaceController.TUYERE_SLOT, 8, 8));
		addSlot(new SlotRestrictedItem(handler, TEBlastFurnaceController.IRON_SLOT, 47, 17,
				Collections.singletonList(Items.IRON_INGOT)));
		addSlot(new SlotRestrictedItem(handler, TEBlastFurnaceController.FLUX_SLOT, 65, 17,
				Collections.singletonList(Items.REDSTONE)));
		addSlot(new SlotRestrictedItem(handler, TEBlastFurnaceController.COKE_SLOTS[0], 47, 53,
				Collections.singletonList(FAItems.coalCoke.ToItem())));
		addSlot(new SlotRestrictedItem(handler, TEBlastFurnaceController.COKE_SLOTS[1], 65, 53,
				Collections.singletonList(FAItems.coalCoke.ToItem())));
		addSlot(new SlotOutputItem(handler, TEBlastFurnaceController.OUTPUT_SLOT, 116, 35));
		addSlot(new SlotOutputItem(handler, TEBlastFurnaceController.SLAG_SLOT, 142, 35));

		int x = 8;
		int y = 84;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlot(new Slot(playerInv, i + j * 9 + 9, x + i * 18, y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(playerInv, i, x + i * 18, 142));
		}
	}

	// client-side constructor
	public ContainerBlastFurnace(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, new ItemStackHandler(7), new IntArray(4), extraData.readBlockPos());
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

			if (fromSlot < this.handler.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.mergeItemStack(current, handler.getSlots(), handler.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
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

	public IIntArray GetProgressBars()
	{
		return progressBars;
	}
}

