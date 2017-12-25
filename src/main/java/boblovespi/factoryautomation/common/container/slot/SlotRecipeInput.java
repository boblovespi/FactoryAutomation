package boblovespi.factoryautomation.common.container.slot;

import boblovespi.factoryautomation.api.recipe.MachineRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 12/24/2017.
 */
public class SlotRecipeInput extends SlotItemHandler
{
	private final MachineRecipeHandler recipeHandler;

	public SlotRecipeInput(IItemHandler itemHandler, int index, int xPosition,
			int yPosition, MachineRecipeHandler recipeHandler)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.recipeHandler = recipeHandler;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return recipeHandler.IsInputValid(stack);
	}
}
