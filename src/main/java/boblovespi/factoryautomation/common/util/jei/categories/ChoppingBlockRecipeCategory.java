package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.jei.wrappers.ChoppingBlockRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Willi on 3/3/2019.
 */
public class ChoppingBlockRecipeCategory implements IRecipeCategory<ChoppingBlockRecipeWrapper>
{
	public static final String ID = "factoryautomation.chopping_block";
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;

	public ChoppingBlockRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"),
						u, v, 83, 54);
	}

	/**
	 * Returns a unique ID for this recipe category.
	 * Referenced from recipes to identify which recipe category they belong to.
	 */
	@Override
	public String getUid()
	{
		return ID;
	}

	/**
	 * Returns the localized name for this recipe type.
	 * Drawn at the top of the recipe GUI pages for this category.
	 */
	@Override
	public String getTitle()
	{
		// noinspection MethodCallSideOnly
		return I18n.format("gui.chopping_block.name");
	}

	/**
	 * Return the mod name or id associated with this recipe category.
	 * Used for the recipe category tab's tooltip.
	 *
	 * @since JEI 4.5.0
	 */
	@Override
	public String getModName()
	{
		return FactoryAutomation.NAME;
	}

	/**
	 * Returns the drawable background for a single recipe in this category.
	 * <p>
	 * The size of the background determines how recipes are laid out by JEI,
	 * make sure it is the right size to contains everything being displayed.
	 */
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	/**
	 * Set the {@link IRecipeLayout} properties from the {@link IRecipeWrapper} and {@link IIngredients}.
	 *
	 * @param recipeLayout  the layout that needs its properties set.
	 * @param recipeWrapper the recipeWrapper, for extra information.
	 * @param ing           the ingredients, already set by the recipeWrapper
	 * @since JEI 3.11.0
	 */
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ChoppingBlockRecipeWrapper recipeWrapper, IIngredients ing)
	{
		List<List<ItemStack>> inputs = ing.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ing.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();

		gui.init(0, true, 55 - u, 35 - v - 1);
		gui.init(1, false, 116 - u, 35 - v - 1);

		gui.set(0, inputs.get(0));
		gui.set(1, outputs.get(0));
	}
}
