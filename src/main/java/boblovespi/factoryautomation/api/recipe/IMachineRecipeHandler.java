package boblovespi.factoryautomation.api.recipe;

import net.minecraft.world.item.ItemStack;

/**
 * Created by Willi on 12/24/2017.
 */
public interface IMachineRecipeHandler
{
	boolean IsInputValid(ItemStack stack);
}
