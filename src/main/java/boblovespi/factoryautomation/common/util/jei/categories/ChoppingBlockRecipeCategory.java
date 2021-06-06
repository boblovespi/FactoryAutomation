package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
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
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Willi on 3/3/2019.
 */
public class ChoppingBlockRecipeCategory implements IRecipeCategory<ChoppingBlockRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "chopping_block");
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;

	public ChoppingBlockRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"),
						u, v, 83, 54);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.woodChoppingBlocks.get(0)));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		// noinspection MethodCallSideOnly
		return I18n.format("gui.chopping_block.name");
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ChoppingBlockRecipe recipe, IIngredients ing)
	{
		List<List<ItemStack>> inputs = ing.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ing.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();

		gui.init(0, true, 55 - u, 35 - v - 1);
		gui.init(1, false, 116 - u, 35 - v - 1);

		gui.set(0, inputs.get(0));
		gui.set(1, outputs.get(0));
	}

	@Override
	public Class<? extends ChoppingBlockRecipe> getRecipeClass()
	{
		return ChoppingBlockRecipe.class;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(ChoppingBlockRecipe recipe, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM,
				Collections.singletonList(Arrays.asList(recipe.getInput().getMatchingStacks())));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}
}
