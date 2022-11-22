package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Created by Willi on 12/23/2017.
 */
public class BlastFurnaceRecipeWrapper
{
	public Ingredient getIngot()
	{
		return Ingredient.of(Items.IRON_INGOT);
	}

	public Ingredient getFlux()
	{
		return Ingredient.of(Items.REDSTONE);
	}

	public ItemStack getOutput()
	{
		return new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON));
	}

	public ItemStack getSlag()
	{
		return new ItemStack(FAItems.slag);
	}
}
