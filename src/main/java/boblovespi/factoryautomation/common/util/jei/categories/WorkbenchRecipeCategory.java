package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchPart;
import boblovespi.factoryautomation.api.recipe.WorkbenchTool;
import boblovespi.factoryautomation.common.block.FABlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
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
public class WorkbenchRecipeCategory implements IRecipeCategory<IWorkbenchRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "workbench");
	private static final int u = 15;
	private static final int v = 16;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;
	private IDrawable icon;

	public WorkbenchRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/workbench.png"), u, v,
						204, 90);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.ironWorkbench));
	}

	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Override
	public Class<? extends IWorkbenchRecipe> getRecipeClass()
	{
		return IWorkbenchRecipe.class;
	}

	@Override
	public String getTitle()
	{
		//noinspection MethodCallSideOnly
		return I18n.get("gui.workbench.name");
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(IWorkbenchRecipe recipe, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM,
				recipe.GetJeiRecipe().stream().map(n -> Arrays.asList(n.getItems()))
					  .collect(Collectors.toList()));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.GetResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IWorkbenchRecipe recipe, IIngredients ingredients)
	{
		IGuiItemStackGroup gui = layout.getItemStacks();
		gui.init(0, false, 197 - u, 52 - v);
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
				if (inputs.get(x + y * 5).getItems().length > 0)
					gui.set(y + x * 5 + 2 + 10, Arrays.asList(inputs.get(x + y * 5).getItems()));
			}
		}

		Iterator<Map.Entry<WorkbenchTool.Instance, Integer>> tools = recipe.GetToolDurabilityUsage().entrySet()
																		   .iterator();
		int y = 2;
		while (tools.hasNext())
		{
			Map.Entry<WorkbenchTool.Instance, Integer> next = tools.next();
			gui.set(y, next.getKey().GetTool().GetItems().keySet().stream().map(n -> {
				ItemStack itemStack = new ItemStack(n, 1);
				itemStack.setDamageValue(next.getValue());
				return itemStack;
			}).collect(Collectors.toList()));
			y++;
		}

		Iterator<Map.Entry<WorkbenchPart.Instance, Integer>> parts = recipe.GetPartUsage().entrySet().iterator();
		y = 7;
		while (parts.hasNext())
		{
			Map.Entry<WorkbenchPart.Instance, Integer> next = parts.next();
			gui.set(
					y, next.getKey().GetPart().GetItems().keySet().stream().map(n -> new ItemStack(n, next.getValue()))
						   .collect(Collectors.toList()));
			y++;
		}

		gui.set(0, recipe.GetResultItem());
	}
}
