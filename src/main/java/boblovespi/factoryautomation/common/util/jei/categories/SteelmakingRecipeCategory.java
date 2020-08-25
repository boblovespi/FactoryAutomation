package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Willi on 5/26/2018.
 */
public class SteelmakingRecipeCategory implements IRecipeCategory<SteelmakingRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "steelmaking_furnace");
	private static final int u = 7;
	private static final int v = 7;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;
	private IDrawable icon;

	public SteelmakingRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/steelmaking_furnace.png"), u, v, 153,
				85);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.steelmakingFurnaceController));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Override
	public Class<? extends SteelmakingRecipe> getRecipeClass()
	{
		return SteelmakingRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		//noinspection MethodCallSideOnly
		return I18n.format("gui.steelmaking_furnace.name");
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(SteelmakingRecipe recipe, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM,
				recipe.GetItemInputs().stream().map(n -> Arrays.asList(n.getMatchingStacks()))
					  .collect(Collectors.toList()));
		if (recipe.GetFluidInputs() != null)
			ingredients.setInputs(VanillaTypes.FLUID, recipe.GetFluidInputs());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.GetPrimaryItemOutputs());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull SteelmakingRecipe recipe, IIngredients ing)
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
