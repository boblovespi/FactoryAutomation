package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Willi on 12/23/2017.
 */
public class BlastFurnaceRecipeCategory implements IRecipeCategory
{
	private IDrawable background;

	public BlastFurnaceRecipeCategory(IGuiHelper helper)
	{
		background = helper.createDrawable(new ResourceLocation(
												   "factoryautomation:textures/gui/container/blast_furnace.png"),
										   4, 4, 168, 78);
	}

	@Override
	public String getUid()
	{
		return "factoryautomation.blast_furnace";
	}

	@Override
	public String getTitle()
	{
		return "gui.blast_furnace.name";
	}

	@Override
	public String getModName()
	{
		return FactoryAutomation.NAME;
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper iRecipeWrapper,
			IIngredients ingredients)
	{
		IGuiItemStackGroup group = layout.getItemStacks();
		group.init(0, true, 42, 12);
		group.init(1, true, 60, 12);
		group.init(2, false, 111, 30);
		group.init(3, false, 137, 30);

		group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		group.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));

		group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		group.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
	}
}
