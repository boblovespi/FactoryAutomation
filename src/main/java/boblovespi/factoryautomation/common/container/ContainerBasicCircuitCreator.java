package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedPredicate;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Collections;

/**
 * Created by Willi on 5/28/2018.
 */
public class ContainerBasicCircuitCreator extends Container
{
	public static final ContainerType<ContainerBasicCircuitCreator> TYPE = IForgeContainerType
			.create(ContainerBasicCircuitCreator::new);
	private final IItemHandler inv;
	private BlockPos pos;

	// server-side container
	public ContainerBasicCircuitCreator(int id, PlayerInventory playerInv, IItemHandler inv, BlockPos pos)
	{
		super(TYPE, id);
		this.inv = inv;
		this.pos = pos;

		addSlot(new SlotOutputItem(inv, 0, 5, 102));
		addSlot(new SlotRestrictedPredicate(inv, 1, 8, 9, Ingredient.fromTag(FATags.ForgeItemTag("nuggets/tin"))));
		addSlot(new SlotRestrictedPredicate(inv, 2, 8, 27, Ingredient.fromTag(FATags.ForgeItemTag("wires/copper"))));
		addSlot(new SlotItemHandler(inv, 3, 8, 45));
		addSlot(new SlotRestrictedItem(inv, 4, 8, 63, Collections.singletonList(FAItems.circuitFrame.ToItem())));
		int x = 24;
		int y = 84;

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

	// client-side constructor
	public ContainerBasicCircuitCreator(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, new ItemStackHandler(5), extraData.readBlockPos());
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
		return pos;
	}

	@Override
	public void updateProgressBar(int id, int data)
	{
		super.updateProgressBar(id, data);
	}
}
