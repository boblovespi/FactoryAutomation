package boblovespi.factoryautomation.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/29/2018.
 */
public class ContainerStoneFoundry extends Container
{
	public static final ContainerType<ContainerStoneFoundry> TYPE = IForgeContainerType
			.create(ContainerStoneFoundry::new);
	private IItemHandler itemHandler;
	private final IIntArray containerInfo;
	private final StringIntArray metalName;

	public ContainerStoneFoundry(int id, PlayerInventory playerInv, IItemHandler inv, IIntArray containerInfo,
			StringIntArray metalName, BlockPos pos)
	{
		super(TYPE, id);
		itemHandler = inv;
		this.containerInfo = containerInfo;
		this.metalName = metalName;
		trackIntArray(containerInfo);
		trackIntArray(metalName);

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

	public ContainerStoneFoundry(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, new ItemStackHandler(2), new IntArray(8), new StringIntArray(8), extraData.readBlockPos());
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

	public int GetBar(int id)
	{
		return containerInfo.get(id);
	}

	public String GetMetalName()
	{
		return metalName.GetString();
	}
}
