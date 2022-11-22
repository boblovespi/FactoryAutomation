package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.FATags;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/26/2018.
 */
public class CampfireRecipe extends ChancelessMachineRecipe
{
	public static final Serializer SERIALIZER = new Serializer();
	public static final RecipeType<CampfireRecipe> TYPE = RecipeType.simple(
			new ResourceLocation(MODID, "campfire"));
	private static final HashMap<String, CampfireRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<Item, CampfireRecipe> ITEM_MAP = new HashMap<>();
	private static final HashMap<String, CampfireRecipe> OREDICT_MAP = new HashMap<>();
	private final ItemStack output;
	private final int time;
	private final String name;
	private final Ingredient input;

	private CampfireRecipe(String name, Ingredient input, ItemStack output, int time)
	{
		super(Collections.singletonList(input), null, Collections.singletonList(output), null);
		this.name = name;
		this.input = input;
		this.output = output;
		this.time = time;
	}

	public static void AddRecipe(String name, String oreName, ItemStack output, int time)
	{
		if (STRING_MAP.containsKey(name))
			return;
		CampfireRecipe recipe = new CampfireRecipe(
				name, Ingredient.of(FATags.ForgeItemTag(oreName)), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put("forge:" + oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, ItemStack output, int time)
	{
		if (STRING_MAP.containsKey(name))
			return;
		CampfireRecipe recipe = new CampfireRecipe(name, Ingredient.of(new ItemStack(item, 1)), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(item, recipe);
	}

	public static CampfireRecipe FindRecipe(ItemStack input)
	{
		if (input.isEmpty())
			return null;

		if (ITEM_MAP.containsKey(input.getItem()))
			return ITEM_MAP.get(input.getItem());
		else
		{
			Set<ResourceLocation> oreIDs = new HashSet<>(); // input.getItem().;
			for (ResourceLocation id : oreIDs)
			{
				if (OREDICT_MAP.containsKey(id.toString()))
					return OREDICT_MAP.get(id.toString());
			}
			return null;
		}
	}

	public static CampfireRecipe GetRecipe(String name)
	{
		return STRING_MAP.getOrDefault(name, null);
	}

	public ItemStack GetOutput()
	{
		return output;
	}

	public Ingredient GetInput()
	{
		return input;
	}

	public String GetName()
	{
		return name;
	}

	public int GetTime()
	{
		return time;
	}

	@Override
	public ResourceLocation getId()
	{
		return new ResourceLocation(name);
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

	public static class Serializer implements RecipeSerializer<CampfireRecipe>
	{
		@Override
		public CampfireRecipe fromJson(ResourceLocation name, JsonObject json)
		{
			Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
			ItemStack output = RecipeHelper.GetItemStackFromObject(json, "output");
			int time = GsonHelper.getAsInt(json, "time");
			return new CampfireRecipe(name.toString(), input, output, time);
		}

		@Nullable
		@Override
		public CampfireRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer)
		{
			var ingredient = Ingredient.fromNetwork(buffer);
			var output = buffer.readItem();
			var time = buffer.readVarInt();
			return new CampfireRecipe(name.toString(), ingredient, output, time);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CampfireRecipe recipe)
		{
			recipe.input.toNetwork(buffer);
			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.time);
		}
	}
}
