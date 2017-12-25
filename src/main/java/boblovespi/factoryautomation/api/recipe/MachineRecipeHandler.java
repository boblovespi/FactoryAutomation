package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * Created by Willi on 12/24/2017.
 */
public abstract class MachineRecipeHandler
{
	public abstract boolean IsInputValid(ItemStack stack);
}
