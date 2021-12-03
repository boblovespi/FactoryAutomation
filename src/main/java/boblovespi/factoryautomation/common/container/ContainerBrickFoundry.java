package boblovespi.factoryautomation.common.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 4/11/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainerBrickFoundry extends AbstractContainerMenu
{
	public static final MenuType<ContainerBrickFoundry> TYPE = IForgeContainerType
			.create(ContainerBrickFoundry::new);
	private final IItemHandler itemHandler;
	private final ContainerData containerInfo;
	private final StringIntArray metalName;

	// server-side constructor
	public ContainerBrickFoundry(int id, Inventory playerInv, IItemHandler inv, ContainerData containerInfo,
			StringIntArray metalName, BlockPos pos)
	{
		super(TYPE, id);
		itemHandler = inv;
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

	// client-side constructor
	public ContainerBrickFoundry(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(2), new SimpleContainerData(9), new StringIntArray(8), extraData.readBlockPos());
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

	public String GetMetalName()
	{
		return metalName.GetString();
	}
}
