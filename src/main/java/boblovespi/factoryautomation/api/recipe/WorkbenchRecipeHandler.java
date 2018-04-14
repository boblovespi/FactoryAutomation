package boblovespi.factoryautomation.api.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * Created by Willi on 4/11/2018.
 */
public class WorkbenchRecipeHandler
{
	public static final HashMap<ResourceLocation, IWorkbenchRecipe> recipes = new HashMap<>(2);

	public static void AddRecipe(ResourceLocation id, IWorkbenchRecipe recipe)
	{
		recipes.put(id, recipe);
	}

	public static void LoadFromJson(ResourceLocation location)
	{
		Gson gson = new GsonBuilder().create();

		location.getResourceDomain()
	}

	private static IWorkbenchRecipe DeserializeFromJson(JsonObject json)
	{
		return null;
	}
}