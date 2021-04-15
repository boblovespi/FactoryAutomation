package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotFuel;
import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.tileentity.TESteelmakingFurnace;
import mcp.MethodsReturnNonnullByDefault;
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

import javax.annotation.ParametersAreNonnullByDefault;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/24/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainerSteelmakingFurnace extends Container
{
	public static final ContainerType<ContainerSteelmakingFurnace> TYPE = IForgeContainerType
			.create(ContainerSteelmakingFurnace::new);
	private final IItemHandler itemHandler;
	private final IIntArray containerInfo;

	// server-side constructor
	public ContainerSteelmakingFurnace(int id, PlayerInventory playerInv, IItemHandler inv, IIntArray containerInfo,
			BlockPos pos)
	{
		super(TYPE, id);
		this.itemHandler = inv;
		this.containerInfo = containerInfo;
		addDataSlots(containerInfo);

		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				addSlot(new SlotItemHandler(itemHandler, TESteelmakingFurnace.INPUT_SLOTS[i * 2 + j], 58 + j * 18,
						20 + i * 18));

				addSlot(new SlotOutputItem(itemHandler, TESteelmakingFurnace.OUTPUT_SLOTS[i * 2 + j], 124 + j * 18,
						20 + i * 18));
			}
		}

		addSlot(new SlotFuel(itemHandler, TESteelmakingFurnace.FUEL_SLOT, 58, 74));

		addSlot(new SlotItemHandler(itemHandler, TESteelmakingFurnace.AIR_INPUT_SLOT, 8, 74));
		addSlot(new SlotItemHandler(itemHandler, TESteelmakingFurnace.FUEL_INPUT_SLOT, 28, 74));

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

	// client-side constructor
	public ContainerSteelmakingFurnace(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, new ItemStackHandler(11), new IntArray(4), extraData.readBlockPos());
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(PlayerEntity playerIn)
	{
		return !playerIn.isSpectator();
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int fromSlot)
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
				if (!this.moveItemStackTo(current, 0, itemHandler.getSlots(), false))
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

	public int GetBar(int id)
	{
		return containerInfo.get(id);
	}
}
