package boblovespi.factoryautomation.common.container.slot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Willi on 4/13/2017.
 *
 * @deprecated use SlotRestrictedPredicate
 */
@Deprecated
public class SlotRestrictedItem extends SlotItemHandler
{
	private final List<Item> items;

	public SlotRestrictedItem(IItemHandler handler, int slotIndex, int xPosition, int yPosition, List<Item> i)
	{
		super(handler, slotIndex, xPosition, yPosition);
		items = i;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean mayPlace(@Nullable ItemStack stack)
	{
		return stack == null || items.contains(stack.getItem());
	}

	public static boolean isBucket(ItemStack stack)
	{
		return stack != null && stack.getItem() != null && stack.getItem() == Items.BUCKET;
	}
}
