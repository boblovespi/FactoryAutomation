package boblovespi.factoryautomation.common.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 12/24/2017.
 */
public class SlotFuel extends SlotItemHandler
{
	public SlotFuel(IItemHandler itemHandler, int index, int xPosition,
			int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return TileEntityFurnace.isItemFuel(stack) || stack.isEmpty();
	}
}
