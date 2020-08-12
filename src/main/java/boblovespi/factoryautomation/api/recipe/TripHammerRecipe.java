package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 8/14/2018.
 */
public class TripHammerRecipe extends ChancelessMachineRecipe
{
	public static final Map<String, TripHammerRecipe> STRING_TRIP_HAMMER_RECIPE_MAP = new HashMap<>();

	public final String name;
	public final Ingredient itemInput;
	public final ItemStack itemOutput;
	public final float time;
	public final float torque;

	public TripHammerRecipe(String name, Ingredient itemInput, ItemStack itemOutput, float time, float torque)
	{
		super(Collections.singletonList(itemInput), null, Collections.singletonList(itemOutput), null);
		this.name = name;
		this.itemInput = itemInput;
		this.itemOutput = itemOutput;
		this.time = time;
		this.torque = torque;
		STRING_TRIP_HAMMER_RECIPE_MAP.put(name, this);
	}

	public static TripHammerRecipe FindRecipe(ItemStack stack)
	{
		for (TripHammerRecipe recipe : STRING_TRIP_HAMMER_RECIPE_MAP.values())
		{
			if (recipe.itemInput.test(stack))
				return recipe;
		}
		return null;
	}
}
