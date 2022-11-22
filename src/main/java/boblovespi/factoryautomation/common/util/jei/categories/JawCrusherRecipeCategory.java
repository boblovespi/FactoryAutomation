package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
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
 * Created by Willi on 5/26/2018.
 */
public class JawCrusherRecipeCategory implements IRecipeCategory<JawCrusherRecipe>
{
	public static final RecipeType<JawCrusherRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "jaw_crusher",
			JawCrusherRecipe.class);
	private static final int u = 54;
	private static final int v = 16;
	private IDrawableStatic background;
	private IGuiHelper guiHelper;
	private IDrawable icon;

	public JawCrusherRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/machine_base.png"), u, v, 83, 54);
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.jawCrusher));
	}

	@Override
	public RecipeType<JawCrusherRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("tile.jaw_crusher.name");
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
	public void setRecipe(IRecipeLayoutBuilder builder, JawCrusherRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 55 - u, 17 - v - 1).addIngredients(recipe.GetItemInputs().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 116 - u, 35 - v - 1)
			   .addItemStack(recipe.GetPrimaryItemOutputs().get(0));
	}
}

