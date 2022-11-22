package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
 * Created by Willi on 12/23/2017.
 */
public class BlastFurnaceRecipeCategory implements IRecipeCategory<BlastFurnaceRecipeWrapper>
{
	public static final RecipeType<BlastFurnaceRecipeWrapper> TYPE = RecipeType.create(FactoryAutomation.MODID,
			"blast_furnace", BlastFurnaceRecipeWrapper.class);
	private IDrawable background;
	private IDrawable icon;

	public BlastFurnaceRecipeCategory(IGuiHelper helper)
	{
		background = helper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/blast_furnace.png"), 4, 4, 168, 78);
		icon = helper.createDrawableItemStack(new ItemStack(FABlocks.blastFurnaceController));
	}

	@Override
	public RecipeType<BlastFurnaceRecipeWrapper> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("gui.blast_furnace.name");
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
	public void setRecipe(IRecipeLayoutBuilder builder, BlastFurnaceRecipeWrapper recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 42, 12).addIngredients(recipe.getIngot());
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 12).addIngredients(recipe.getFlux());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 30).addItemStack(recipe.getOutput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 30).addItemStack(recipe.getSlag());
	}
}
