package boblovespi.factoryautomation.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.HashSet;

/**
 * Created by Willi on 8/13/2018.
 */
public class RestrictedSlotItemHandler extends ItemStackHandler
{
	private HashSet<Integer> restrictedSlots;
	private ItemStackHandler wrappingHandler;

	public RestrictedSlotItemHandler(HashSet<Integer> restrictedSlots, ItemStackHandler wrappingHandler)
	{
		this.restrictedSlots = restrictedSlots;
		this.wrappingHandler = wrappingHandler;
	}

	private boolean IsInternalSlotValid(int slot)
	{
		return !restrictedSlots.contains(slot);
	}

	@Override
	public void setSize(int size)
	{
		wrappingHandler.setSize(size);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
		if (!IsInternalSlotValid(slot))
			return;
		wrappingHandler.setStackInSlot(slot, stack);
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
	public NBTTagCompound serializeNBT()
	{
		return wrappingHandler.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		wrappingHandler.deserializeNBT(nbt);
	}
}
