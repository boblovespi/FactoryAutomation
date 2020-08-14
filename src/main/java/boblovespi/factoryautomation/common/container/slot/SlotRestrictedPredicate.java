package boblovespi.factoryautomation.common.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 6/6/2018.
 */
public class SlotRestrictedPredicate extends SlotItemHandler
{
	private final Ingredient predicate;

	public SlotRestrictedPredicate(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			Ingredient predicate)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.predicate = predicate;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return predicate.test(stack);
	}
}
