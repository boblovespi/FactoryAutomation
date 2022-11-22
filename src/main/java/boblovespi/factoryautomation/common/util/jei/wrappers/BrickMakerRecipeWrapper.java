package boblovespi.factoryautomation.common.util.jei.wrappers;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.crafting.Ingredient;

public class BrickMakerRecipeWrapper
{
	public Ingredient getInput()
	{
		return Ingredient.of(FAItems.terraclay);
	}

	public Ingredient getOutput()
	{
		return Ingredient.of(FAItems.terraclayBrick);
	}
}
