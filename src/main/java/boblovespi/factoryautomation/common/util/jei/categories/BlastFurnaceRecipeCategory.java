package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 12/23/2017.
 */
public class BlastFurnaceRecipeCategory implements IRecipeCategory<BlastFurnaceRecipeWrapper>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "blast_furnace");
	private IDrawable background;
	private IDrawable icon;

	public BlastFurnaceRecipeCategory(IGuiHelper helper)
	{
		background = helper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/blast_furnace.png"), 4,
						4, 168, 78);
		icon = helper.createDrawableIngredient(new ItemStack(FABlocks.blastFurnaceController));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Nonnull
	@Override
	public Class<? extends BlastFurnaceRecipeWrapper> getRecipeClass()
	{
		return BlastFurnaceRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return "gui.blast_furnace.name";
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
	public void setIngredients(BlastFurnaceRecipeWrapper recipe, IIngredients ingredients)
	{
		recipe.fillIngredients(ingredients);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, BlastFurnaceRecipeWrapper recipe, IIngredients ingredients)
	{
		IGuiItemStackGroup group = layout.getItemStacks();
		group.init(0, true, 42, 12);
		group.init(1, true, 60, 12);
		group.init(2, false, 111, 30);
		group.init(3, false, 137, 30);

		group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		group.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));

		group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		group.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
	}
}
