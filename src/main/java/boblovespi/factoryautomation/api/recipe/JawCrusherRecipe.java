package boblovespi.factoryautomation.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Created by Willi on 2/20/2018.
 * jaw crusher recipes
 */
public class JawCrusherRecipe implements IMachineRecipe
{
	private static Map<String, JawCrusherRecipe> recipeMap = new HashMap<>(8);
	public final String name;
	public final float speedReq;
	public final float torqueReq;
	public final float time;
	public final Function<Float, ItemStack> output;
	private final int plateTier;
	private Ingredient input;

	public JawCrusherRecipe(Ingredient input, Map<Float, ItemStack> output, int plateTier, String name, float speedReq,
			float torqueReq, float time)
	{
		final float[] tweight = { 0 };
		output.keySet().forEach(n -> tweight[0] += n);
		this.input = input;
		this.output = weight ->
		{
			weight *= tweight[0];
			for (float w : output.keySet())
			{
				weight -= w;
				if (weight <= 0)
					return output.get(w).copy();
			}
			return output.isEmpty() ? ItemStack.EMPTY : (ItemStack) output.values().toArray()[0];
		};

		this.plateTier = plateTier;
		this.name = name;
		this.speedReq = speedReq;
		this.torqueReq = torqueReq;
		this.time = time;
	}

	public JawCrusherRecipe(Ingredient input, Function<Float, ItemStack> output, int plateTier, String name,
			float speedReq, float torqueReq, float time)
	{
		this.input = input;
		this.output = output;

		this.plateTier = plateTier;
		this.name = name;
		this.speedReq = speedReq;
		this.torqueReq = torqueReq;
		this.time = time;
	}

	public JawCrusherRecipe(Ingredient input, ItemStack output, int plateTier, String name,
			float speedReq, float torqueReq, float time)
	{
		this.input = input;
		this.output = n -> output;

		this.plateTier = plateTier;
		this.name = name;
		this.speedReq = speedReq;
		this.torqueReq = torqueReq;
		this.time = time;
	}

	public static void AddRecipe(JawCrusherRecipe recipe)
	{
		recipeMap.put(recipe.name, recipe);
	}

	public static JawCrusherRecipe FindRecipe(ItemStack input, int tier)
	{
		for (JawCrusherRecipe recipe : recipeMap.values())
		{
			if (recipe.input.test(input.copy()) && tier >= recipe.plateTier)
			{
				return recipe;
			}
		}
		return null;
	}

	public static JawCrusherRecipe GetRecipe(String key)
	{
		return recipeMap.get(key);
	}

	public static Collection<JawCrusherRecipe> GetRecipes()
	{
		return recipeMap.values();
	}

	/**
	 * @return A list of all the item inputs
	 */
	@Override
	public List<Ingredient> GetItemInputs()
	{
		return Collections.singletonList(input);
	}

	/**
	 * @return A list of all the fluid inputs
	 */
	@Override
	public List<FluidStack> GetFluidInputs()
	{
		return null;
	}

	/**
	 * @return A list of all the item outputs with a 100% chance of being produced
	 */
	@Override
	public List<ItemStack> GetPrimaryItemOutputs()
	{
		return Collections.singletonList(output.apply(0.5f));
	}

	/**
	 * @return A list of all the fluid outputs with a 100% chance of being produced
	 */
	@Override
	public List<FluidStack> GetPrimaryFluidOutputs()
	{
		return null;
	}

	/**
	 * @return A list of all the item outputs with chances. Null if there are none
	 */
	@Nullable
	@Override
	public Map<ItemStack, Float> GetSecondaryItemOutputs()
	{
		return null;
	}

	/**
	 * @return A list of all the fluid outputs with chances. Null if there are none
	 */
	@Nullable
	@Override
	public Map<FluidStack, Float> GetSecondaryFluidOutputs()
	{
		return null;
	}

	@Override
	public void WriteToNBT(CompoundTag tag)
	{
	}

	@Override
	public ResourceLocation getId()
	{
		return null;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return null;
	}

	@Override
	public RecipeType<?> getType()
	{
		return null;
	}

	public ItemStack GetOutput(float n)
	{
		return output.apply(n);
	}
}
