package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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

import javax.annotation.Nonnull;

/**
 * Created by Willi on 3/3/2019.
 */
public class ChoppingBlockRecipeCategory implements IRecipeCategory<ChoppingBlockRecipe>
{
	public static final RecipeType<ChoppingBlockRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID,
			"chopping_block", ChoppingBlockRecipe.class);
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
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.woodChoppingBlocks.get(0)));
	}

	@Override
	public RecipeType<ChoppingBlockRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("jei.chopping_block");
	}

	@Nonnull
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
	public void setRecipe(IRecipeLayoutBuilder builder, ChoppingBlockRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 54 - u, 35 - v - 1).addIngredients(recipe.GetInput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 115 - u, 35 - v - 1).addItemStack(recipe.GetOutput());
	}
}
