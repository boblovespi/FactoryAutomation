package boblovespi.factoryautomation.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeHelper
{
	private RecipeHelper()
	{

	}

	public static ItemStack[] GetStacksFromArray(JsonArray array)
	{
		var result = new ItemStack[array.size()];
		for (int i = 0; i < array.size(); i++)
		{
			if (array.get(i).isJsonObject()) result[i] = ShapedRecipe.itemStackFromJson(array.get(i).getAsJsonObject());
			else result[i] = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(array.get(i)
																									   .getAsString())));
		}
		return result;
	}

	public static ItemStack[] GetStacksFromObject(JsonObject object, String key)
	{
		if (object.get(key).isJsonArray()) return GetStacksFromArray(object.getAsJsonArray(key));
		else if (object.get(key).isJsonObject())
			return new ItemStack[] {ShapedRecipe.itemStackFromJson(object.get(key).getAsJsonObject())};
		else return new ItemStack[] {new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.get(key)
																											   .getAsString())))};
	}

	public static ItemStack[] GetStacksFromBuffer(FriendlyByteBuf buffer)
	{
		int n = buffer.readVarInt();
		var result = new ItemStack[n];
		for (int i = 0; i < n; i++)
		{
			result[i] = buffer.readItem();
		}
		return result;
	}

	public static void WriteStacksToBuffer(ItemStack[] output, FriendlyByteBuf buffer)
	{
		buffer.writeVarInt(output.length);
		for (ItemStack stack : output)
		{
			buffer.writeItem(stack);
		}
	}
}
