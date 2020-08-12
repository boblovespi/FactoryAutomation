package boblovespi.factoryautomation.common.util.recipes;

import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.util.Randoms;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 6/30/2018.
 */
public class HammerRecipe extends ShapedRecipe
{
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
			ItemStack stack = inv.getStackInSlot(i).copy();
			if (stack.getItem() instanceof Hammer)
			{
				boolean b = stack.attemptDamageItem(1, Randoms.MAIN.r, null);
				if (b)
					stack.shrink(1);
				list.set(i, stack);
			}
		}
		return list;
	}
}
