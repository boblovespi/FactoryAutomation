package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.block.crafter.workbench.Workbench;
import boblovespi.factoryautomation.common.util.Log;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 4/11/2018.
 */
public class WorkbenchRecipeHandler
{
	public static final HashMap<ResourceLocation, IWorkbenchRecipe> recipes = new HashMap<>(50);
	public static final IRecipeType<IWorkbenchRecipe> WORKBENCH_RECIPE_TYPE = IRecipeType.register("workbench_recipe");
	public static final ShapedSerializer SHAPED_SERIALIZER = new ShapedSerializer();

	public static void AddRecipe(ResourceLocation id, IWorkbenchRecipe recipe)
	{
		recipes.put(id, recipe);
	}

	/*
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
		//					JsonObject json = JSONUtils.fromJson(gson, reader, JsonObject.class);
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
							JsonObject[] json = JSONUtils.fromJson(gson, reader, JsonObject[].class);
							Method loadContext = ctx.getClass().getDeclaredMethod("loadConstants", JsonObject[].class);
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
						JsonObject json = JSONUtils.fromJson(gson, reader, JsonObject.class);
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
	}*/

	private static void AddFromJson(JsonObject json, ResourceLocation id)
	{
		// System.out.println("json = [" + json + "], context = [" + context + "], id = [" + id + "]");
		ResourceLocation type = new ResourceLocation(JSONUtils.getAsString(json, "type"));
		if (type.equals(new ResourceLocation("factoryautomation", "workbench_shaped")))
		{
			AddRecipe(id, DeserializeShapedFromJson(json));
		}
	}

	private static IWorkbenchRecipe DeserializeShapedFromJson(JsonObject json)
	{
		int tier = JSONUtils.getAsInt(json, "tier");
		JsonArray pattern = JSONUtils.getAsJsonArray(json, "pattern");
		Map<String, Ingredient> key = DeserializeKeyFromJson(JSONUtils.getAsJsonObject(json, "key"));

		Ingredient[][] ingredients = DeserializePatternFromJson(pattern, key);

		JsonObject toolJson = JSONUtils.getAsJsonObject(json, "tools");
		JsonObject partJson = JSONUtils.getAsJsonObject(json, "parts");

		HashMap<WorkbenchTool.Instance, Integer> tools = DeserializeToolsFromJson(toolJson);
		HashMap<WorkbenchPart.Instance, Integer> parts = DeserializePartsFromJson(partJson);
		ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);

		return new ShapedWorkbenchRecipe(tier, ingredients, tools, parts, result);
	}

	private static Map<String, Ingredient> DeserializeKeyFromJson(JsonObject key)
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

			ingMap.put(String.valueOf(entry.getKey().toCharArray()[0]), CraftingHelper.getIngredient(entry.getValue()));
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
				JsonObject toolInfo = JSONUtils.getAsJsonObject(entry.getValue().getAsJsonObject(), entry.getKey());
				WorkbenchTool.Instance toolInstance = WorkbenchTool.Instance
						.FromTool(tool, JSONUtils.getAsInt(toolInfo, "tier"));
				int durabilityUse = JSONUtils.getAsInt(toolInfo, "durabilityUse");
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
				JsonObject partInfo = JSONUtils.getAsJsonObject(entry.getValue().getAsJsonObject(), entry.getKey());
				WorkbenchPart.Instance partInstance = WorkbenchPart.Instance
						.FromPart(part, JSONUtils.getAsInt(partInfo, "tier"));
				int itemUse = JSONUtils.getAsInt(partInfo, "itemUse");
				map.put(partInstance, itemUse);
			}
		}

		return map;
	}

	private static HashMap<WorkbenchTool.Instance, Integer> DeserializeToolsFromBuffer(PacketBuffer buffer)
	{
		HashMap<WorkbenchTool.Instance, Integer> map = new HashMap<>();
		int l = buffer.readByte();
		for (int i = 0; i < l; i++)
		{
			ResourceLocation toolId = buffer.readResourceLocation();
			WorkbenchTool tool = WorkbenchTool.tools.get(toolId);
			if (tool == null)
			{
				Log.LogWarning(toolId + " is not a registered tool, but is used in a recipe!");
				buffer.readByte();
				buffer.readByte();
			} else
			{
				WorkbenchTool.Instance toolInstance = WorkbenchTool.Instance.FromTool(tool, buffer.readByte());
				map.put(toolInstance, (int) buffer.readByte());
			}
		}

		return map;
	}

	private static HashMap<WorkbenchPart.Instance, Integer> DeserializePartsFromBuffer(PacketBuffer buffer)
	{
		HashMap<WorkbenchPart.Instance, Integer> map = new HashMap<>();
		int l = buffer.readByte();
		for (int i = 0; i < l; i++)
		{
			ResourceLocation toolId = buffer.readResourceLocation();
			WorkbenchPart tool = WorkbenchPart.parts.get(toolId);
			if (tool == null)
			{
				Log.LogWarning(toolId + " is not a registered tool, but is used in a recipe!");
				buffer.readByte();
				buffer.readByte();
			} else
			{
				WorkbenchPart.Instance toolInstance = WorkbenchPart.Instance.FromPart(tool, buffer.readByte());
				map.put(toolInstance, (int) buffer.readByte());
			}
		}

		return map;
	}

	public static class ShapedSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<ShapedWorkbenchRecipe>
	{
		@Override
		public ShapedWorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return (ShapedWorkbenchRecipe) DeserializeShapedFromJson(json).setRegistryName(recipeId);
		}

		@Nullable
		@Override
		public ShapedWorkbenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
		{
			int tier = buffer.readInt();
			int sizeX = buffer.readInt();
			int sizeY = buffer.readInt();
			Ingredient[][] ingredients = new Ingredient[sizeY][sizeX];
			ItemStack result = buffer.readItem();
			for (int i = 0; i < sizeY; i++)
			{
				for (int j = 0; j < sizeX; j++)
				{
					ingredients[i][j] = Ingredient.fromNetwork(buffer);
				}
			}
			HashMap<WorkbenchTool.Instance, Integer> tools = DeserializeToolsFromBuffer(buffer);
			HashMap<WorkbenchPart.Instance, Integer> parts = DeserializePartsFromBuffer(buffer);
			return (ShapedWorkbenchRecipe) new ShapedWorkbenchRecipe(tier, ingredients, tools, parts, result).setRegistryName(recipeId);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, ShapedWorkbenchRecipe recipe)
		{
			buffer.writeInt(recipe.GetTier());
			buffer.writeInt(recipe.GetSizeX());
			buffer.writeInt(recipe.GetSizeY());
			buffer.writeItemStack(recipe.GetResultItem(), true);
			for (int i = 0; i < recipe.GetRecipe().length; i++)
			{
				for (int j = 0; j < recipe.GetRecipe()[i].length; j++)
				{
					recipe.GetRecipe()[i][j].toNetwork(buffer);
				}
			}
			buffer.writeByte(recipe.GetToolDurabilityUsage().size());
			for (Map.Entry<WorkbenchTool.Instance, Integer> pair : recipe.GetToolDurabilityUsage().entrySet())
			{
				buffer.writeResourceLocation(pair.getKey().GetTool().GetId());
				buffer.writeByte(pair.getKey().tier);
				buffer.writeByte(pair.getValue());
			}
			buffer.writeByte(recipe.GetPartUsage().size());
			for (Map.Entry<WorkbenchPart.Instance, Integer> pair : recipe.GetPartUsage().entrySet())
			{
				buffer.writeResourceLocation(pair.getKey().GetPart().GetId());
				buffer.writeByte(pair.getKey().tier);
				buffer.writeByte(pair.getValue());
			}
		}
	}
}