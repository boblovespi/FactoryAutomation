package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchPart;
import boblovespi.factoryautomation.api.recipe.WorkbenchTool;
import boblovespi.factoryautomation.common.util.jei.wrappers.WorkbenchRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Willi on 5/5/2018.
 */
public class WorkbenchRecipeCategory implements IRecipeCategory<WorkbenchRecipeWrapper>
{
	private static final int u = 15;
	private static final int v = 16;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;

	public WorkbenchRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/workbench.png"), u, v,
						204, 90);
	}

	/**
	 * Returns a unique ID for this recipe category.
	 * Referenced from recipes to identify which recipe category they belong to.
	 */
	@Override
	public String getUid()
	{
		return "factoryautomation.workbench";
	}

	/**
	 * Returns the localized name for this recipe type.
	 * Drawn at the top of the recipe GUI pages for this category.
	 */
	@Override
	public String getTitle()
	{
		//noinspection MethodCallSideOnly
		return I18n.format("gui.workbench.name");
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
	 * @param layout      the layout that needs its properties set.
	 * @param wrapper     the recipeWrapper, for extra information.
	 * @param ingredients the ingredients, already set by the recipeWrapper
	 * @since JEI 3.11.0
	 */
	@Override
	public void setRecipe(IRecipeLayout layout, WorkbenchRecipeWrapper wrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup gui = layout.getItemStacks();
		gui.init(0, false, 197 - u, 52 - v);
		IWorkbenchRecipe recipe = wrapper.getRecipe();
		List<Ingredient> inputs = recipe.GetJeiRecipe();

		for (int i = 0; i < 7; i++) // init all the slots using black magic
		{
			for (int j = 0; j < 5; j++)
			{
				gui.init(j + i * 5 + 2, true, 15 + (i < 1 ? 0 : 26 + (i < 2 ? 0 : 26 + (i - 2) * 18)) - u,
						16 + 18 * j - v);
			}
		}

		for (int x = 0; x < 5; x++) // set the main recipe slots
		{
			for (int y = 0; y < 5; y++)
			{
				if (inputs.get(x + y * 5).getMatchingStacks().length > 0)
					gui.set(y + x * 5 + 2 + 10, Arrays.asList(inputs.get(x + y * 5).getMatchingStacks()));
			}
		}

		Iterator<Map.Entry<WorkbenchTool.Instance, Integer>> tools = recipe.GetToolDurabilityUsage().entrySet()
																		   .iterator();
		int y = 2;
		while (tools.hasNext())
		{
			Map.Entry<WorkbenchTool.Instance, Integer> next = tools.next();
			gui.set(y,
					next.getKey().GetTool().GetItems().keySet().stream().map(n -> new ItemStack(n, 1, next.getValue()))
						.collect(Collectors.toList()));
			y++;
		}

		Iterator<Map.Entry<WorkbenchPart.Instance, Integer>> parts = recipe.GetPartUsage().entrySet().iterator();
		y = 7;
		while (parts.hasNext())
		{
			Map.Entry<WorkbenchPart.Instance, Integer> next = parts.next();
			gui.set(y, next.getKey().GetPart().GetItems().keySet().stream().map(n -> new ItemStack(n, next.getValue()))
						   .collect(Collectors.toList()));
			y++;
		}

		gui.set(0, recipe.GetResultItem());
	}
}
