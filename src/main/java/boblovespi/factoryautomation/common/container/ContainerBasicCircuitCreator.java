package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedPredicate;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

/**
 * Created by Willi on 5/28/2018.
 */
@ParametersAreNonnullByDefault
public class ContainerBasicCircuitCreator extends AbstractContainerMenu
{
	public static final MenuType<ContainerBasicCircuitCreator> TYPE = IForgeMenuType.create(ContainerBasicCircuitCreator::new);
	private final IItemHandler inv;
	private final BlockPos pos;

	// server-side container
	public ContainerBasicCircuitCreator(int id, Inventory playerInv, IItemHandler inv, BlockPos pos)
	{
		super(TYPE, id);
		this.inv = inv;
		this.pos = pos;

		addSlot(new SlotOutputItem(inv, 0, 5, 102));
		addSlot(new SlotRestrictedPredicate(inv, 1, 8, 9, Ingredient.of(FATags.ForgeItemTag("nuggets/tin"))));
		addSlot(new SlotRestrictedPredicate(inv, 2, 8, 27, Ingredient.of(FATags.ForgeItemTag("wires/copper"))));
		addSlot(new SlotItemHandler(inv, 3, 8, 45));
		addSlot(new SlotRestrictedItem(inv, 4, 8, 63, Collections.singletonList(FAItems.circuitFrame)));
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
	public ContainerBasicCircuitCreator(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(5), extraData.readBlockPos());
	}

	/**
	 * Determines whether supplied player can use this container
	 */
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
				slot.set(
						ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else
				slot.setChanged();

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
	public void setData(int id, int data)
	{
		super.setData(id, data);
	}
}
