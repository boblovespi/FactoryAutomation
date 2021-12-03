package boblovespi.factoryautomation.common.util.recipes;

import boblovespi.factoryautomation.common.util.Randoms;
import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Willi on 12/26/2018.
 */
public class AxeRecipe extends ShapedRecipe
{
	public static final Serializer SERIALIZER = new Serializer();

	public AxeRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
			NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
	{
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv)
	{
		NonNullList<ItemStack> list = super.getRemainingItems(inv);
		for (int i = 0; i < list.size(); i++)
		{
			ItemStack stack = inv.getItem(i).copy();
			if (stack.getItem() instanceof AxeItem)
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
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
			implements RecipeSerializer<AxeRecipe>
	{
		@Override
		public AxeRecipe fromJson(ResourceLocation recipeId, JsonObject json)
		{
			return AxeRecipe.From(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
		}

		@Nullable
		@Override
		public AxeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			return AxeRecipe.From(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, AxeRecipe recipe)
		{
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}

	private static AxeRecipe From(ShapedRecipe read)
	{
		return new AxeRecipe(
				read.getId(), read.getGroup(), read.getRecipeWidth(), read.getRecipeHeight(), read.getIngredients(),
				read.getResultItem());
	}
}