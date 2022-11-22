package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
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

import javax.annotation.Nonnull;

/**
 * Created by Willi on 5/26/2018.
 */
public class SteelmakingRecipeCategory implements IRecipeCategory<SteelmakingRecipe>
{
	public static final RecipeType<SteelmakingRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID,
			"steelmaking_furnace", SteelmakingRecipe.class);
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
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.steelmakingFurnaceController));
	}

	@Nonnull
	@Override
	public RecipeType<SteelmakingRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("gui.steelmaking_furnace.name");
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
	public void setRecipe(IRecipeLayoutBuilder builder, SteelmakingRecipe recipe, IFocusGroup focuses)
	{
		IRecipeSlotBuilder[] slots = new IRecipeSlotBuilder[8];

		slots[0] = builder.addSlot(RecipeIngredientRole.INPUT, 58 - u - 1, 20 - v - 1);
		slots[1] = builder.addSlot(RecipeIngredientRole.INPUT, 76 - u - 1, 20 - v - 1);
		slots[2] = builder.addSlot(RecipeIngredientRole.INPUT, 58 - u - 1, 38 - v - 1);
		slots[3] = builder.addSlot(RecipeIngredientRole.INPUT, 76 - u - 1, 38 - v - 1);

		slots[4] = builder.addSlot(RecipeIngredientRole.OUTPUT, 124 - u - 1, 20 - v - 1);
		slots[5] = builder.addSlot(RecipeIngredientRole.OUTPUT, 142 - u - 1, 20 - v - 1);
		slots[6] = builder.addSlot(RecipeIngredientRole.OUTPUT, 124 - u - 1, 38 - v - 1);
		slots[7] = builder.addSlot(RecipeIngredientRole.OUTPUT, 142 - u - 1, 38 - v - 1);

		var inputs = recipe.GetItemInputs();
		for (int i = 0; i < inputs.size(); i++)
			slots[i].addIngredients(inputs.get(i));

		var outputs = recipe.GetPrimaryItemOutputs();
		for (int i = 0; i < outputs.size(); i++)
			slots[i + 4].addItemStack(outputs.get(i));
	}
}
