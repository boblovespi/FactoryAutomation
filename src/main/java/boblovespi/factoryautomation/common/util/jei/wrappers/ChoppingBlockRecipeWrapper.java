package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Willi on 3/3/2019.
 */
public class ChoppingBlockRecipeWrapper implements IRecipeWrapper
{
	private final ChoppingBlockRecipe recipe;

	public ChoppingBlockRecipeWrapper(ChoppingBlockRecipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(
				VanillaTypes.ITEM, recipe.GetItemInputs().stream().map(n -> Arrays.asList(n.getMatchingStacks()))
										 .collect(Collectors.toList()));
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.GetPrimaryItemOutputs());
	}
}
