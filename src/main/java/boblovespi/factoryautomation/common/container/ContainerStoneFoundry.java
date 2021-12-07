package boblovespi.factoryautomation.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 12/29/2018.
 */
@ParametersAreNonnullByDefault
public class ContainerStoneFoundry extends AbstractContainerMenu
{
	public static final MenuType<ContainerStoneFoundry> TYPE = IForgeMenuType.create(ContainerStoneFoundry::new);
	private final IItemHandler itemHandler;
	private final ContainerData containerInfo;
	private final StringIntArray metalName;

	public ContainerStoneFoundry(int id, Inventory playerInv, IItemHandler inv, ContainerData containerInfo, StringIntArray metalName, BlockPos pos)
	{
		super(TYPE, id);
		this.itemHandler = inv;
		this.containerInfo = containerInfo;
		this.metalName = metalName;
		addDataSlots(containerInfo);
		addDataSlots(metalName);

		addSlot(new SlotItemHandler(itemHandler, 1, 67, 18));
		addSlot(new SlotItemHandler(itemHandler, 0, 67, 60));

		int x = 8;
		int y = 98;

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

	public ContainerStoneFoundry(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(2), new SimpleContainerData(7), new StringIntArray(8), extraData.readBlockPos());
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

			if (fromSlot < this.itemHandler.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.moveItemStackTo(current, itemHandler.getSlots(), itemHandler.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.moveItemStackTo(current, 0, itemHandler.getSlots(), false)) return ItemStack.EMPTY;
			}

			if (current.isEmpty()) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
				slot.set(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
			else slot.setChanged();

			if (current.getCount() == previous.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, current);

		}
		return previous;
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(Player playerIn)
	{
		return !playerIn.isSpectator();
	}

	public int GetBar(int id)
	{
		return containerInfo.get(id);
	}

	public String GetMetalName()
	{
		return metalName.GetString();
	}
}
