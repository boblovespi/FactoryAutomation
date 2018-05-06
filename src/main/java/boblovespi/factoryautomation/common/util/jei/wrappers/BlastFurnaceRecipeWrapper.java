package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

/**
 * Created by Willi on 12/23/2017.
 */
public class BlastFurnaceRecipeWrapper implements IRecipeWrapper
{

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(ItemStack.class,
				Arrays.asList(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE)));
		ingredients.setOutputs(ItemStack.class,
				Arrays.asList(new ItemStack(FAItems.ingot.ToItem(), 1, Metals.PIG_IRON.GetId()),
						new ItemStack(FAItems.slag.ToItem())));
	}
}
