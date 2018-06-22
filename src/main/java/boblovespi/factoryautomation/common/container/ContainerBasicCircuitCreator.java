package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedPredicate;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Arrays;

/**
 * Created by Willi on 5/28/2018.
 */
public class ContainerBasicCircuitCreator extends Container
{
	private TEBasicCircuitCreator te;
	private IItemHandler inv;

	public ContainerBasicCircuitCreator(IInventory playerInv, TileEntity te)
	{
		this.te = (TEBasicCircuitCreator) te;
		inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		addSlotToContainer(new SlotOutputItem(inv, 0, 5, 102));
		addSlotToContainer(new SlotRestrictedPredicate(inv, 1, 8, 9, new OreIngredient("nuggetTin")));
		addSlotToContainer(new SlotRestrictedPredicate(inv, 2, 8, 27,
				new OreIngredient("wireCopper")));
		addSlotToContainer(new SlotItemHandler(inv, 3, 8, 45));
		addSlotToContainer(new SlotRestrictedItem(inv, 4, 8, 63, Arrays.asList(FAItems.circuitFrame.ToItem())));
		int x = 24;
		int y = 84;

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
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !playerIn.isSpectator();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot)
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

	public boolean HasFrame()
	{
		return !inv.getStackInSlot(4).isEmpty();
	}

	public BlockPos GetPos()
	{
		return te.getPos();
	}
}
