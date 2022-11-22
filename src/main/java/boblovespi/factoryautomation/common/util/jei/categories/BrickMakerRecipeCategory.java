package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.wrappers.BrickMakerRecipeWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BrickMakerRecipeCategory implements IRecipeCategory<BrickMakerRecipeWrapper>
{
	public static final RecipeType<BrickMakerRecipeWrapper> TYPE = RecipeType.create(FactoryAutomation.MODID,
			"brick_maker", BrickMakerRecipeWrapper.class);
	private static final MutableComponent text = Component.literal("150 seconds").withStyle(ChatFormatting.DARK_GRAY);
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;

	public BrickMakerRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"), u, v, 83, 54);
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.brickMakerFrame));
	}

	@Override
	public RecipeType<BrickMakerRecipeWrapper> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("jei.brick_maker");
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
	public void setRecipe(IRecipeLayoutBuilder builder, BrickMakerRecipeWrapper recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 54 - u, 35 - v - 1).addIngredients(recipe.getInput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 115 - u, 35 - v - 1).addIngredients(recipe.getOutput());
	}

	@Override
	public void draw(BrickMakerRecipeWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX,
					 double mouseY)
	{
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
		IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
	}
}
