package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchPart;
import boblovespi.factoryautomation.api.recipe.WorkbenchTool;
import boblovespi.factoryautomation.common.block.FABlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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
	public static final RecipeType<IWorkbenchRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "workbench",
			IWorkbenchRecipe.class);
	private static final int u = 15;
	private static final int v = 16;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;
	private IDrawable icon;

	public WorkbenchRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/workbench.png"), u, v, 204, 90);
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.ironWorkbench));
	}

	@Override
	public RecipeType<IWorkbenchRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Override
	public Component getTitle()
	{
		return Component.translatable("jei.workbench");
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
	public void setRecipe(IRecipeLayoutBuilder builder, IWorkbenchRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.OUTPUT, 197 - u, 52 - v).addItemStack(recipe.GetResultItem());
		List<Ingredient> inputs = recipe.GetJeiRecipe();
		IRecipeSlotBuilder[] slots = new IRecipeSlotBuilder[7 * 5];

		for (int i = 0; i < 7; i++) // init all the slots using black magic
		{
			for (int j = 0; j < 5; j++)
			{
				slots[j + i * 5] = builder.addSlot(RecipeIngredientRole.INPUT,
						15 + (i < 1 ? 0 : 26 + (i < 2 ? 0 : 26 + (i - 2) * 18)) - u, 16 + 18 * j - v);
			}
		}

		for (int x = 0; x < 5; x++) // set the main recipe slots
		{
			for (int y = 0; y < 5; y++)
			{
				if (inputs.get(x + y * 5).getItems().length > 0)
					slots[y + x * 5 + 10].addIngredients(inputs.get(x + y * 5));
			}
		}

		Iterator<Map.Entry<WorkbenchTool.Instance, Integer>> tools = recipe.GetToolDurabilityUsage().entrySet()
																		   .iterator();
		int y = 2;
		while (tools.hasNext())
		{
			Map.Entry<WorkbenchTool.Instance, Integer> next = tools.next();
			slots[y].addItemStacks(next.getKey().GetTool().GetItems().entrySet().stream()
									   .filter(n -> n.getValue() >= next.getKey().tier).map(Map.Entry::getKey)
									   .map(n -> {
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
			slots[y].addItemStacks(next.getKey().GetPart().GetItems().entrySet().stream()
									   .filter(n -> n.getValue() >= next.getKey().tier).map(Map.Entry::getKey)
									   .map(n -> new ItemStack(n, next.getValue())).collect(Collectors.toList()));
			y++;
		}
	}
}
