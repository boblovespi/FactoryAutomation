package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 12/25/2017.
 */
public class SteelmakingRecipe extends ChancelessMachineRecipe
{
	private static Map<String, SteelmakingRecipe> recipeMap = new HashMap<>(8);

	public final float timeRequired;
	public final float tempRequired;

	public SteelmakingRecipe(List<Ingredient> itemInputs,
			List<FluidStack> fluidInputs, List<ItemStack> itemOutputs,
			float timeRequired, float tempRequired)
	{
		super(itemInputs, fluidInputs, itemOutputs, null);
		this.timeRequired = timeRequired;
		this.tempRequired = tempRequired;
		assert itemInputs.size() <= 4;
	}

	public static void AddRecipe(String key, SteelmakingRecipe recipe)
	{
		recipeMap.put(key, recipe);
	}

	public static SteelmakingRecipe FindRecipe(List<ItemStack> inputs,
			List<FluidStack> fluids)
	{
		Collection<SteelmakingRecipe> recipes = recipeMap.values();

		for (SteelmakingRecipe recipe : recipes)
		{
			boolean isCorrectRecipe = true;
			if (inputs.size() == recipe.itemInputs.size())
			{
				for (int i = 0; i < inputs.size(); i++)
				{
					isCorrectRecipe = recipe.itemInputs.get(i)
													   .apply(inputs.get(i));
					if (!isCorrectRecipe)
						break;
				}
			} else
				continue;
			if (!isCorrectRecipe)
				continue;
			if ((fluids != null ? fluids.size() : 0) == (
					recipe.fluidInputs != null ? recipe.fluidInputs.size() : 0))
			{
				if (fluids != null && recipe.fluidInputs != null)
				{
					for (int i = 0; i < fluids.size(); i++)
					{
						isCorrectRecipe = fluids.get(i).containsFluid(
								recipe.fluidInputs.get(i));
						if (!isCorrectRecipe)
							break;
					}
				} else
				{
					isCorrectRecipe = true;
				}
			} else
				continue;
			if (isCorrectRecipe)
				return recipe;
		}

		return null;
	}
}
