package boblovespi.factoryautomation.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.BitSet;

/**
 * Created by Willi on 8/13/2018.
 */
public class RestrictedSlotItemHandler implements IItemHandler
{
	private final BitSet restrictedSlots;
	private final ItemStackHandler wrappingHandler;

	public RestrictedSlotItemHandler(BitSet restrictedSlots, ItemStackHandler wrappingHandler)
	{
		this.restrictedSlots = restrictedSlots;
		this.wrappingHandler = wrappingHandler;
	}

	private boolean IsInternalSlotValid(int slot)
	{
		return !restrictedSlots.get(slot);
	}

	@Override
	public int getSlots()
	{
		return wrappingHandler.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (!IsInternalSlotValid(slot))
			return ItemStack.EMPTY;
		return wrappingHandler.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (!IsInternalSlotValid(slot))
			return stack;
		return wrappingHandler.insertItem(slot, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (!IsInternalSlotValid(slot))
			return ItemStack.EMPTY;
		return wrappingHandler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		if (!IsInternalSlotValid(slot))
			return 0;
		return wrappingHandler.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return IsInternalSlotValid(slot);
	}
}
