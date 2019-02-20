package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
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
		ingredients.setInputs(VanillaTypes.ITEM,
				Arrays.asList(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE)));
		ingredients.setOutputs(
				VanillaTypes.ITEM, Arrays.asList(new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON)),
						new ItemStack(FAItems.slag.ToItem())));
	}
}
