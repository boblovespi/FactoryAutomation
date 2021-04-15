package boblovespi.factoryautomation.common.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Willi on 4/13/2017.
 */
public class SlotOutputItem extends SlotItemHandler
{
	public SlotOutputItem(IItemHandler itemHandler, int index, int xPosition,
		    int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
	}

	/**
	 * since this is an output, you cannot put stuff in it
	 *
	 * @param stack item stack to check.
	 * @return if it may place.
	 */
	@Override
	public boolean mayPlace(ItemStack stack)
	{
		return false;
	}
}
