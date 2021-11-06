package boblovespi.factoryautomation.common.util.recipes;

import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.util.Randoms;
import com.google.gson.JsonObject;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/30/2018.
 */
public class HammerRecipe extends ShapedRecipe
{
	public static final Serializer SERIALIZER = new Serializer();

	public HammerRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
			NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
	{
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv)
	{
		NonNullList<ItemStack> list = super.getRemainingItems(inv);
		for (int i = 0; i < list.size(); i++)
		{
			ItemStack stack = inv.getItem(i).copy();
			if (stack.getItem() instanceof Hammer)
			{
				boolean b = stack.hurt(1, Randoms.MAIN.r, null);
				if (b)
					stack.shrink(1);
				list.set(i, stack);
			}
		}
		return list;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<HammerRecipe>
	{
		@Override
		public HammerRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return (HammerRecipe) IRecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
		}

		@Nullable
		@Override
		public HammerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
		{
			return (HammerRecipe) IRecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, HammerRecipe recipe)
		{
			IRecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
}
