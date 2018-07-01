package boblovespi.factoryautomation.common.util.recipes;

import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.util.Randoms;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 6/30/2018.
 */
public class HammerRecipe extends ShapedOreRecipe
{
	public HammerRecipe(ResourceLocation group, Block result, Object... recipe)
	{
		super(group, result, recipe);
	}

	public HammerRecipe(ResourceLocation group, Item result, Object... recipe)
	{
		super(group, result, recipe);
	}

	public HammerRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
	{
		super(group, result, recipe);
	}

	public HammerRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer)
	{
		super(group, result, primer);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
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
