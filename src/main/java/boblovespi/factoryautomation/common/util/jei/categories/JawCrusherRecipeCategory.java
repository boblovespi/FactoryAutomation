package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.jei.wrappers.JawCrusherRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by Willi on 5/26/2018.
 */
public class JawCrusherRecipeCategory implements IRecipeCategory<JawCrusherRecipeWrapper>
{
	public static final String ID = "factoryautomation.jaw_crusher";
	private static final int u = 54;
	private static final int v = 16;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;

	public JawCrusherRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/machine_base.png"), u, v,
						83, 54);
	}

	@Override
	public String getUid()
	{
		return ID;
	}

	@Override
	public String getTitle()
	{
		//noinspection MethodCallSideOnly
		return I18n.format("gui.steelmaking_furnace.name");
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
	public void setRecipe(IRecipeLayout recipeLayout, JawCrusherRecipeWrapper recipeWrapper, IIngredients ing)
	{
		List<List<ItemStack>> inputs = ing.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ing.getOutputs(ItemStack.class);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();

		gui.init(0, true, 55 - u, 17 - v - 1);
		gui.init(1, false, 116 - u, 35 - v - 1);

		gui.set(0, inputs.get(0));
		gui.set(1, outputs.get(0));
	}
}

