package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Willi on 5/5/2018.
 */
public class WorkbenchRecipeWrapper implements IRecipeWrapper
{
	private IWorkbenchRecipe recipe;

	public WorkbenchRecipeWrapper(IWorkbenchRecipe recipe)
	{
		this.recipe = recipe;
	}

	/**
	 * Gets all the recipe's ingredients by filling out an instance of {@link IIngredients}.
	 */
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(
				VanillaTypes.ITEM, recipe.GetJeiRecipe().stream().map(n -> Arrays.asList(n.getMatchingStacks()))
										 .collect(Collectors.toList()));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.GetResultItem());
	}

	public IWorkbenchRecipe getRecipe()
	{
		return recipe;
	}
}
