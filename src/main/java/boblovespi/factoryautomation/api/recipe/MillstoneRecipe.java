package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.FATags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 2/12/2019.
 */
public class MillstoneRecipe extends ChancelessMachineRecipe
{
	public static final Serializer SERIALIZER = new MillstoneRecipe.Serializer();
	public static final RecipeType<MillstoneRecipe> TYPE = RecipeType.register(MODID + ":millstone");
	private static final HashMap<String, MillstoneRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<Item, MillstoneRecipe> ITEM_MAP = new HashMap<>();
	private static final HashMap<String, MillstoneRecipe> OREDICT_MAP = new HashMap<>();
	private final ItemStack[] output;
	private final String name;
	private final Ingredient input;
	private final int time;
	private final float torque;

	private MillstoneRecipe(String name, Ingredient input, int time, float torque, ItemStack... output)
	{
		super(Collections.singletonList(input), null, Arrays.asList(output), null);
		this.name = name;
		this.input = input;
		this.time = time;
		this.torque = torque;
		this.output = output;
	}

	public static void AddRecipe(String name, String oreName, int time, float torque, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		MillstoneRecipe recipe = new MillstoneRecipe(name, Ingredient.of(FATags.ForgeItemTag(oreName)), time, torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put("forge:" + oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, int time, float torque, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		MillstoneRecipe recipe = new MillstoneRecipe(name, Ingredient.of(new ItemStack(item, 1)), time,
													 torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(item, recipe);
	}

	public static MillstoneRecipe FindRecipe(ItemStack input)
	{
		if (input.isEmpty())
			return null;
		if (ITEM_MAP.containsKey(input.getItem()))
			return ITEM_MAP.get(input.getItem());
		else
		{
			Set<ResourceLocation> oreIDs = input.getItem().getTags();
			for (ResourceLocation id : oreIDs)
			{
				if (OREDICT_MAP.containsKey(id.toString()))
					return OREDICT_MAP.get(id.toString());
			}
			return null;
		}
	}

	public static MillstoneRecipe GetRecipe(String name)
	{
		return STRING_MAP.getOrDefault(name, null);
	}

	public ItemStack[] GetOutputs()
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

	public float GetTorque()
	{
		return torque;
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

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MillstoneRecipe>
	{
		@Override
		public MillstoneRecipe fromJson(ResourceLocation name, JsonObject json)
		{
			JsonElement element = GsonHelper.isArrayNode(json, "input") ? GsonHelper.getAsJsonArray(json, "input") : GsonHelper.getAsJsonObject(json, "input");
			Ingredient input = Ingredient.fromJson(element);
			ItemStack[] output = RecipeHelper.GetStacksFromObject(json, "output");
			int time = GsonHelper.getAsInt(json, "time");
			float torque = GsonHelper.getAsFloat(json, "torque");
			return new MillstoneRecipe(name.toString(), input, time, torque, output);
		}

		@Nullable
		@Override
		public MillstoneRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer)
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack[] output = RecipeHelper.GetStacksFromBuffer(buffer);
			int time = buffer.readVarInt();
			float torque = buffer.readFloat();
			return new MillstoneRecipe(name.toString(), input, time, torque, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, MillstoneRecipe recipe)
		{
			recipe.input.toNetwork(buffer);
			RecipeHelper.WriteStacksToBuffer(recipe.output, buffer);
			buffer.writeVarInt(recipe.time);
			buffer.writeFloat(recipe.torque);
		}
	}
}
