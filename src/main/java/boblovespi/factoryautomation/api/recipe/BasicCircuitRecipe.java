package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator.Layout.Element;
import boblovespi.factoryautomation.common.util.Log;
import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
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

/**
 * Created by Willi on 6/17/2018.
 */
public class BasicCircuitRecipe
{
	public static final HashMap<ResourceLocation, BasicCircuitRecipe> recipes = new HashMap<>(2);
	private final ItemStack result;
	private Element[][] recipe = new Element[8][8];

	public BasicCircuitRecipe(ItemStack result, Element[][] recPattern)
	{
		this.result = result;
		for (int i = 0; i < recipe.length; i++)
		{
			System.arraycopy(recPattern[i], 0, recipe[i], 0, recipe[0].length);
		}
	}

	public static void AddRecipe(ResourceLocation id, BasicCircuitRecipe recipe)
	{
		recipes.put(id, recipe);
	}

	public static void LoadFromJson(ModContainer mod, ResourceLocation location)
	{
		Gson gson = new GsonBuilder().create();
		JsonContext ctx = new JsonContext(location.getResourceDomain());
		ModMetadata dummyModMeta = new ModMetadata();
		dummyModMeta.name = location.getResourceDomain();
		dummyModMeta.modId = location.toString();
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
						JsonObject json = JsonUtils.fromJson(gson, reader, JsonObject.class);
						AddFromJson(json, key, ctx);
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

	private static void AddFromJson(JsonObject json, ResourceLocation key, JsonContext ctx)
	{
		ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
		if (type.equals(new ResourceLocation("factoryautomation", "basic_circuit")))
		{
			AddRecipe(key, DeserializeFromJson(json, ctx));
		}
	}

	private static BasicCircuitRecipe DeserializeFromJson(JsonObject json, JsonContext ctx)
	{
		JsonArray pattern = JsonUtils.getJsonArray(json, "pattern");
		Element[][] recPattern = new Element[8][8];

		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				recPattern[x][y] = Element.values()[MathHelper
						.clamp(Integer.valueOf(pattern.get(x).getAsString().substring(y, y + 1)), 0,
								Element.values().length)];
			}
		}

		ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), ctx);
		return new BasicCircuitRecipe(result, recPattern);
	}

	public static BasicCircuitRecipe FindRecipe(Element[][] grid)
	{
		for (BasicCircuitRecipe rec : recipes.values())
		{
			if (rec.Matches(grid))
				return rec;
		}
		return null;
	}

	public boolean Matches(Element[][] grid)
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (recipe[i][j] != grid[i][j])
					return false;
			}
		}
		return true;
	}

	public ItemStack GetResult()
	{
		return result.copy();
	}
}
