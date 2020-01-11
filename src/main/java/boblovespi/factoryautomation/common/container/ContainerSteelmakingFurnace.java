package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotFuel;
import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Willi on 12/24/2017.
 */
public class ContainerSteelmakingFurnace extends Container
{
	private TESteelmakingFurnace te;
	private IItemHandler itemHandler;
	private IFluidHandler fluidHandler;

	public ContainerSteelmakingFurnace(IInventory playerInv, TileEntity te)
	{
		itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

		this.te = (TESteelmakingFurnace) te;

		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				addSlotToContainer(
						new SlotItemHandler(itemHandler, TESteelmakingFurnace.INPUT_SLOTS[i * 2 + j], 58 + j * 18,
								20 + i * 18));

				addSlotToContainer(
						new SlotOutputItem(itemHandler, TESteelmakingFurnace.OUTPUT_SLOTS[i * 2 + j], 124 + j * 18,
								20 + i * 18));
			}
		}

		addSlotToContainer(new SlotFuel(itemHandler, TESteelmakingFurnace.FUEL_SLOT, 58, 74));

		addSlotToContainer(new SlotItemHandler(itemHandler, TESteelmakingFurnace.AIR_INPUT_SLOT, 8, 74));
		addSlotToContainer(new SlotItemHandler(itemHandler, TESteelmakingFurnace.FUEL_INPUT_SLOT, 28, 74));

		int x = 8;
		int y = 98;

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

	/**
	 * Determines whether supplied player can use this container
	 */
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

			if (fromSlot < this.itemHandler.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.mergeItemStack(current, itemHandler.getSlots(), itemHandler.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.mergeItemStack(current, 0, itemHandler.getSlots(), false))
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
