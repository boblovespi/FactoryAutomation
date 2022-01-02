package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.common.item.FAItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;

public class BrickMakerRecipeWrapper
{
	public void fillIngredients(IIngredients ingredients)
	{
		ingredients.setInput(VanillaTypes.ITEM, new ItemStack(FAItems.terraclay));
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(FAItems.terraclayBrick));
	}
}
