package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.Log;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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

	public static void LoadFromJson(ModContainer mod, ResourceLocation location)
	{
		System.out.println("WorkbenchRecipeHandler.LoadFromJson");
		System.out.println("mod = [" + mod + "], location = [" + location + "]");
		//		try
		//		{
		Gson gson = new GsonBuilder().create();
		//
		//			URL resource = WorkbenchRecipeHandler.class
		//					.getResource("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());
		//
		//			Path path;
		//			try
		//			{
		//				path = Paths.get(resource.toURI());
		//			} catch (URISyntaxException e)
		//			{
		//				Log.getLogger().error("Error in trying to load recipes for resourcelocation " + location.toString());
		//				return;
		//			}
		//
		//			Iterator<Path> iterator;
		//
		//			iterator = Files.walk(path).iterator();
		//
		//			while (iterator.hasNext())
		//			{
		//				Path jsonPath = iterator.next();
		//
		//				BufferedReader reader;
		//				try
		//				{
		//					reader = Files.newBufferedReader(jsonPath);
		//					JsonObject json = JsonUtils.fromJson(gson, reader, JsonObject.class);
		//
		//					AddFromJson(json);
		//
		//				} catch (IOException e)
		//				{
		//					Log.getLogger().error("Error in trying to load recipe");
		//					System.out.println("jsonPath = " + jsonPath);
		//					e.printStackTrace();
		//				}
		//
		//			}
		//		} catch (Exception e)
		//		{
		//			e.printStackTrace();
		//		}

		JsonContext ctx = new JsonContext(location.getResourceDomain());
		ModMetadata dummyModMeta = new ModMetadata();
		dummyModMeta.name = location.getResourceDomain();
		dummyModMeta.modId = location.toString();

		ModContainer dummyMod = new DummyModContainer(dummyModMeta)
		{
			//			@Override
			//			public File getSource()
			//			{
			//				return Loader.instance().getActiveModList().get(0);
			//			}
		};

		CraftingHelper
				.findFiles(mod, "assets/" + location.getResourceDomain() + "/" + location.getResourcePath(), root ->
				{
					Path fPath = root.resolve("_constants.json");
					if (fPath != null && Files.exists(fPath))
					{
						BufferedReader reader = null;
						try
						{
							reader = Files.newBufferedReader(fPath);
							JsonObject[] json = JsonUtils.fromJson(gson, reader, JsonObject[].class);
							Method loadContext = ctx.getClass().getDeclaredMethod("loadContext", JsonObject[].class);
							loadContext.setAccessible(true);
							loadContext.invoke(ctx, (Object) json);
						} catch (IOException e)
						{
							Log.LogError("Error loading _constants.json: " + e);
							return false;
						} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
						{
							Log.LogError("Reflection failed");
							e.printStackTrace();
							return false;
						} finally
						{
							IOUtils.closeQuietly(reader);
						}
					}
					return true;
				}, (root, file) ->
				{
					String relative = root.relativize(file).toString();
					if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
						return true;

					String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
					ResourceLocation key = new ResourceLocation(ctx.getModId(), name);

					BufferedReader reader = null;
					try
					{
						reader = Files.newBufferedReader(file);
						JsonObject json = JsonUtils.fromJson(gson, reader, JsonObject.class);
						AddFromJson(json, ctx, key);
					} catch (JsonParseException e)
					{
						FMLLog.log.error("Parsing error loading recipe {}", key, e);
						return false;
					} catch (IOException e)
					{
						FMLLog.log.error("Couldn't read recipe {} from {}", key, file, e);
						return false;
					} finally
					{
						IOUtils.closeQuietly(reader);
					}
					return true;
				}, true, true);
	}

	private static void AddFromJson(JsonObject json, JsonContext context, ResourceLocation id)
	{
		System.out.println("json = [" + json + "], context = [" + context + "], id = [" + id + "]");
		ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
		if (type.equals(new ResourceLocation("factoryautomation", "workbench_shaped")))
		{
			AddRecipe(id, DeserializeShapedFromJson(json, context));
		}
	}

	private static IWorkbenchRecipe DeserializeShapedFromJson(JsonObject json, JsonContext context)
	{
		int tier = JsonUtils.getInt(json, "tier");
		JsonArray pattern = JsonUtils.getJsonArray(json, "pattern");
		Map<String, Ingredient> key = DeserializeKeyFromJson(JsonUtils.getJsonObject(json, "key"), context);

		Ingredient[][] ingredients = DeserializePatternFromJson(pattern, key);

		JsonObject toolJson = JsonUtils.getJsonObject(json, "tools");
		JsonObject partJson = JsonUtils.getJsonObject(json, "parts");

		HashMap<WorkbenchTool.Instance, Integer> tools = DeserializeToolsFromJson(toolJson);
		HashMap<WorkbenchPart.Instance, Integer> parts = DeserializePartsFromJson(partJson);
		ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

		return new ShapedWorkbenchRecipe(tier, ingredients, tools, parts, result);
	}

	private static Map<String, Ingredient> DeserializeKeyFromJson(JsonObject key, JsonContext context)
	{
		Map<String, Ingredient> ingMap = Maps.newHashMap();

		ingMap.put(" ", Ingredient.EMPTY);

		for (Map.Entry<String, JsonElement> entry : key.entrySet())
		{
			if (entry.getKey().length() != 1)
				throw new JsonSyntaxException(
						"Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			if (" ".equals(entry.getKey()))
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

			ingMap.put(String.valueOf(entry.getKey().toCharArray()[0]),
					CraftingHelper.getIngredient(entry.getValue(), context));
		}
		return ingMap;
	}

	private static Ingredient[][] DeserializePatternFromJson(JsonArray pattern, Map<String, Ingredient> key)
	{
		Ingredient[][] ingredients = new Ingredient[pattern.size()][pattern.get(0).getAsString().length()];

		for (int x = 0; x < ingredients.length; x++)
		{
			for (int y = 0; y < ingredients[0].length; y++)
			{
				ingredients[x][y] = key
						.getOrDefault(String.valueOf(pattern.get(x).getAsString().charAt(y)), Ingredient.EMPTY);
			}
		}

		return ingredients;
	}

	private static IWorkbenchRecipe DeserializeShapelessFromJson(JsonObject json)
	{
		throw new UnsupportedOperationException("Shapeless recipes not yet implemented");
	}

	private static HashMap<WorkbenchTool.Instance, Integer> DeserializeToolsFromJson(JsonObject tools)
	{
		HashMap<WorkbenchTool.Instance, Integer> map = new HashMap<>();
		for (Map.Entry<String, JsonElement> entry : tools.entrySet())
		{
			WorkbenchTool tool = WorkbenchTool.tools.get(new ResourceLocation(entry.getKey()));
			if (tool == null)
			{
				Log.LogWarning(entry.getKey() + " is not a registered tool, but is used in a recipe!");
			} else
			{
				JsonObject toolInfo = JsonUtils.getJsonObject(entry.getValue(), entry.getKey());
				WorkbenchTool.Instance toolInstance = WorkbenchTool.Instance
						.FromTool(tool, JsonUtils.getInt(toolInfo, "tier"));
				int durabilityUse = JsonUtils.getInt(toolInfo, "durabilityUse");
				map.put(toolInstance, durabilityUse);
			}
		}

		return map;
	}

	private static HashMap<WorkbenchPart.Instance, Integer> DeserializePartsFromJson(JsonObject parts)
	{
		HashMap<WorkbenchPart.Instance, Integer> map = new HashMap<>();
		for (Map.Entry<String, JsonElement> entry : parts.entrySet())
		{
			WorkbenchPart part = WorkbenchPart.parts.get(new ResourceLocation(entry.getKey()));
			if (part == null)
			{
				Log.LogWarning(entry.getKey() + " is not a registered part, but is used in a recipe!");
			} else
			{
				JsonObject partInfo = JsonUtils.getJsonObject(entry.getValue(), entry.getKey());
				WorkbenchPart.Instance partInstance = WorkbenchPart.Instance
						.FromPart(part, JsonUtils.getInt(partInfo, "tier"));
				int itemUse = JsonUtils.getInt(partInfo, "itemUse");
				map.put(partInstance, itemUse);
			}
		}

		return map;
	}
}