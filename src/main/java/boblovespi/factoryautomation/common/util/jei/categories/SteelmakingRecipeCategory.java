package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
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
 * Created by Willi on 5/26/2018.
 */
public class SteelmakingRecipeCategory implements IRecipeCategory
{
	public static final String ID = "factoryautomation.steelmaking";
	private static final int u = 7;
	private static final int v = 7;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;

	public SteelmakingRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/steelmaking_furnace.png"), u, v, 153,
				85);
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
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ing)
	{
		// TODO: add fluids!

		List<List<ItemStack>> inputs = ing.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ing.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();
		gui.init(0, true, 58 - u - 1, 20 - v - 1);
		gui.init(1, true, 76 - u - 1, 20 - v - 1);
		gui.init(2, true, 58 - u - 1, 38 - v - 1);
		gui.init(3, true, 76 - u - 1, 38 - v - 1);

		gui.init(4, false, 124 - u - 1, 20 - v - 1);
		gui.init(5, false, 142 - u - 1, 20 - v - 1);
		gui.init(6, false, 124 - u - 1, 38 - v - 1);
		gui.init(7, false, 142 - u - 1, 38 - v - 1);

		for (int i = 0; i < inputs.size(); i++)
		{
			gui.set(i, inputs.get(i));
		}

		for (int i = 0; i < outputs.size(); i++)
		{
			gui.set(i + 4, outputs.get(i));
		}

	}
}
