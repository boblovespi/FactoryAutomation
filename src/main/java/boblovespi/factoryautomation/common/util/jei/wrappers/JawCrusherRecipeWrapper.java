package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Willi on 5/26/2018.
 * jaw crusher recipe class
 */
public class JawCrusherRecipeWrapper implements IRecipeWrapper
{
	private final JawCrusherRecipe recipe;

	public JawCrusherRecipeWrapper(JawCrusherRecipe recipe)
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
