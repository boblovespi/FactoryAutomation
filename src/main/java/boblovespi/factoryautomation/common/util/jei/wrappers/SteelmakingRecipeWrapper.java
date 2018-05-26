package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Willi on 5/26/2018.
 */
public class SteelmakingRecipeWrapper implements IRecipeWrapper
{
	private final SteelmakingRecipe recipe;

	public SteelmakingRecipeWrapper(SteelmakingRecipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(
				ItemStack.class, recipe.GetItemInputs().stream().map(n -> Arrays.asList(n.getMatchingStacks()))
									   .collect(Collectors.toList()));
		if (recipe.GetFluidInputs() != null)
			ingredients.setInputs(FluidStack.class, recipe.GetFluidInputs());
		ingredients.setOutputs(ItemStack.class, recipe.GetPrimaryItemOutputs());
	}
}
