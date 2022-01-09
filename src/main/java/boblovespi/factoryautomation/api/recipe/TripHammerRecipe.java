package boblovespi.factoryautomation.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 8/14/2018.
 */
public class TripHammerRecipe extends ChancelessMachineRecipe
{
	public static final Serializer SERIALIZER = new Serializer();
	public static final RecipeType<TripHammerRecipe> TYPE = RecipeType.register(MODID + ":trip_hammer");
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

	@Override
	public RecipeType<?> getType()
	{
		return TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	@Override
	public ResourceLocation getId()
	{
		return new ResourceLocation(name);
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TripHammerRecipe>
	{
		@Override
		public TripHammerRecipe fromJson(ResourceLocation name, JsonObject json)
		{
			Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
			ItemStack output = RecipeHelper.GetItemStackFromObject(json, "output");
			float time = GsonHelper.getAsFloat(json, "time");
			float torque = GsonHelper.getAsFloat(json, "torque");
			return new TripHammerRecipe(name.toString(), input, output, time, torque);
		}

		@Nullable
		@Override
		public TripHammerRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer)
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			var time = buffer.readFloat();
			var torque = buffer.readFloat();
			return new TripHammerRecipe(name.toString(), input, output, time, torque);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TripHammerRecipe recipe)
		{
			recipe.itemInput.toNetwork(buffer);
			buffer.writeItem(recipe.itemOutput);
			buffer.writeFloat(recipe.time);
			buffer.writeFloat(recipe.torque);
		}
	}
}
