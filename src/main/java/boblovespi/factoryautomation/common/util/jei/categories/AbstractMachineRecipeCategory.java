package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.api.recipe.IMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractMachineRecipeCategory<T extends IMachineRecipe> implements IRecipeCategory<T>
{
	/*@Override
	public void setIngredients(T recipe, IIngredients ingredients)
	{
		if (!recipe.GetItemInputs().isEmpty() && !recipe.GetItemInputs().get(0).isEmpty())
			ingredients.setInputLists(VanillaTypes.ITEM, recipe.GetItemInputs().stream().map(Ingredient::getItems).map(Arrays::asList).toList());
		ingredients.setInputLists(VanillaTypes.FLUID, recipe.GetFluidInputs().stream().map(List::of).toList());
		if (!recipe.GetPrimaryItemOutputs().isEmpty() && !recipe.GetPrimaryItemOutputs().get(0).isEmpty())
		{
			if (recipe.GetSecondaryItemOutputs() == null)
				ingredients.setOutputs(VanillaTypes.ITEM, recipe.GetPrimaryItemOutputs());
			else
			{
				var items = new ArrayList<>(recipe.GetPrimaryItemOutputs());
				items.addAll(recipe.GetSecondaryItemOutputs().keySet());
				ingredients.setOutputs(VanillaTypes.ITEM, items);
			}
		}
		if (recipe.GetSecondaryFluidOutputs() == null)
			ingredients.setOutputs(VanillaTypes.FLUID, recipe.GetPrimaryFluidOutputs());
		else
		{
			var fluids = new ArrayList<>(recipe.GetPrimaryFluidOutputs());
			fluids.addAll(recipe.GetSecondaryFluidOutputs().keySet());
			ingredients.setOutputs(VanillaTypes.FLUID, fluids);
		}
	}*/
}
