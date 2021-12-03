package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator.Layout.Element;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by Willi on 6/17/2018.
 */
public class BasicCircuitRecipe implements Recipe<Container>
{
	public static final HashMap<ResourceLocation, BasicCircuitRecipe> recipes = new HashMap<>(2);
	public static final RecipeType<BasicCircuitRecipe> TYPE = RecipeType
			.register(FactoryAutomation.MODID + ":basic_circuit_creator");
	public static final Serializer SERIALIZER = new Serializer();
	private final ResourceLocation id;
	private final ItemStack result;
	private Element[][] recipe = new Element[8][8];

	public BasicCircuitRecipe(ResourceLocation id, ItemStack result, Element[][] recPattern)
	{
		this.id = id;
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

	//	public static void LoadFromJson(ModContainer mod, ResourceLocation location)
	//	{
	//		Gson gson = new GsonBuilder().create();
	//		JsonContext ctx = new JsonContext(location.getResourceDomain());
	//		ModMetadata dummyModMeta = new ModMetadata();
	//		dummyModMeta.name = location.getResourceDomain();
	//		dummyModMeta.modId = location.toString();
	//		CraftingHelper
	//				.findFiles(mod, "assets/" + location.getResourceDomain() + "/" + location.getResourcePath(), root -> {
	//					Path fPath = root.resolve("_constants.json");
	//					if (fPath != null && Files.exists(fPath))
	//					{
	//						BufferedReader reader = null;
	//						try
	//						{
	//							reader = Files.newBufferedReader(fPath);
	//							JsonObject[] json = JsonUtils.fromJson(gson, reader, JsonObject[].class);
	//							Method loadContext = ctx.getClass().getDeclaredMethod("loadConstants", JsonObject[].class);
	//							loadContext.setAccessible(true);
	//							loadContext.invoke(ctx, (Object) json);
	//						} catch (IOException e)
	//						{
	//							Log.LogError("Error loading _constants.json: " + e);
	//							return false;
	//						} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
	//						{
	//							Log.LogError("Reflection failed");
	//							e.printStackTrace();
	//							return false;
	//						} finally
	//						{
	//							IOUtils.closeQuietly(reader);
	//						}
	//					}
	//					return true;
	//				}, (root, file) -> {
	//					String relative = root.relativize(file).toString();
	//					if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
	//						return true;
	//
	//					String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
	//					ResourceLocation key = new ResourceLocation(ctx.getModId(), name);
	//
	//					BufferedReader reader = null;
	//					try
	//					{
	//						reader = Files.newBufferedReader(file);
	//						JsonObject json = JsonUtils.fromJson(gson, reader, JsonObject.class);
	//						AddFromJson(json, key, ctx);
	//					} catch (JsonParseException e)
	//					{
	//						FMLLog.log.error("Parsing error loading recipe {}", key, e);
	//						return false;
	//					} catch (IOException e)
	//					{
	//						FMLLog.log.error("Couldn't read recipe {} from {}", key, file, e);
	//						return false;
	//					} finally
	//					{
	//						IOUtils.closeQuietly(reader);
	//					}
	//					return true;
	//				}, true, true);
	//	}

	//	private static void AddFromJson(JsonObject json, ResourceLocation key, JsonContext ctx)
	//	{
	//		ResourceLocation type = new ResourceLocation(JSONUtils.getString(json, "type"));
	//		if (type.equals(new ResourceLocation("factoryautomation", "basic_circuit")))
	//		{
	//			AddRecipe(key, DeserializeFromJson(json, ctx));
	//		}
	//	}

	private static BasicCircuitRecipe DeserializeFromJson(ResourceLocation id, JsonObject json)
	{
		JsonArray pattern = GsonHelper.getAsJsonArray(json, "pattern");
		Element[][] recPattern = new Element[8][8];

		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				recPattern[x][y] = Element.values()[Mth
						.clamp(Integer.parseInt(pattern.get(x).getAsString().substring(y, y + 1)), 0,
								Element.values().length)];
			}
		}

		ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
		return new BasicCircuitRecipe(id, result, recPattern);
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

	@Override
	public boolean matches(Container inv, Level levelIn)
	{
		return false;
	}

	@Override
	public ItemStack assemble(Container inv)
	{
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem()
	{
		return result;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return TYPE;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
			implements RecipeSerializer<BasicCircuitRecipe>
	{
		private static final ResourceLocation NAME = new ResourceLocation(
				FactoryAutomation.MODID, "basic_circuit");

		@Override
		public BasicCircuitRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return DeserializeFromJson(recipeId, json);
		}

		@Nullable
		@Override
		public BasicCircuitRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			ItemStack itemStack = buffer.readItem();
			byte[] bytes = buffer.readByteArray(64);
			Element[][] elements = new Element[8][8];
			for (int x = 0; x < 8; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					elements[x][y] = Element.values()[bytes[8 * x + y]];
				}
			}
			return new BasicCircuitRecipe(recipeId, itemStack, elements);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BasicCircuitRecipe recipe)
		{
			buffer.writeItemStack(recipe.result, true);
			byte[] elements = new byte[64];
			for (int x = 0; x < 8; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					elements[8 * x + y] = (byte) recipe.recipe[x][y].ordinal();
				}
			}
			buffer.writeByteArray(elements);
		}
	}
}
