package boblovespi.factoryautomation.common.container;

import boblovespi.factoryautomation.common.container.slot.SlotOutputItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedItem;
import boblovespi.factoryautomation.common.container.slot.SlotRestrictedPredicate;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEBlastFurnaceController;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 11/12/2017.
 * Created by Willi on 4/13/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainerBlastFurnace extends AbstractContainerMenu
{
	public static final MenuType<ContainerBlastFurnace> TYPE = IForgeContainerType
			.create(ContainerBlastFurnace::new);
	private TEBlastFurnaceController te;
	private final IItemHandler handler;
	private final ContainerData progressBars;

	// server-side container
	public ContainerBlastFurnace(int id, Inventory playerInv, IItemHandler inv, ContainerData progressBars,
			BlockPos pos)
	{
		super(TYPE, id);
		handler = inv;
		this.progressBars = progressBars;
		addDataSlots(progressBars);

		addSlot(new SlotItemHandler(handler, TEBlastFurnaceController.TUYERE_SLOT, 8, 8));
		addSlot(new SlotRestrictedPredicate(handler, TEBlastFurnaceController.IRON_SLOT, 47, 17,
				Ingredient.of(Items.IRON_INGOT)));
		addSlot(new SlotRestrictedPredicate(handler, TEBlastFurnaceController.FLUX_SLOT, 65, 17,
				Ingredient.of(Items.REDSTONE)));
		addSlot(new SlotRestrictedPredicate(handler, TEBlastFurnaceController.COKE_SLOTS[0], 47, 53,
				Ingredient.of(FAItems.coalCoke.ToItem())));
		addSlot(new SlotRestrictedPredicate(handler, TEBlastFurnaceController.COKE_SLOTS[1], 65, 53,
				Ingredient.of(FAItems.coalCoke.ToItem())));
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
	public ContainerBlastFurnace(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(7), new SimpleContainerData(4), extraData.readBlockPos());
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

			if (fromSlot < this.handler.getSlots())
			{
				// From the block breaker inventory to player's inventory
				if (!this.moveItemStackTo(current, handler.getSlots(), handler.getSlots() + 36, true))
					return ItemStack.EMPTY;
			} else
			{
				// From the player's inventory to block breaker's inventory
				if (!this.moveItemStackTo(current, 0, handler.getSlots(), false))
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

	public ContainerData GetProgressBars()
	{
		return progressBars;
	}
}

