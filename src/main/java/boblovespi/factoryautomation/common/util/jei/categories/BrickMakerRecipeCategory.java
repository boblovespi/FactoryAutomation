package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.wrappers.BrickMakerRecipeWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class BrickMakerRecipeCategory implements IRecipeCategory<BrickMakerRecipeWrapper>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "brick_maker");
	private static final MutableComponent text = new TextComponent("150 seconds").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;

	public BrickMakerRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"),
								u, v, 83, 54);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.brickMakerFrame));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return new TranslatableComponent("jei.brick_maker");
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BrickMakerRecipeWrapper recipe, IIngredients ing)
	{
		List<List<ItemStack>> inputs = ing.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ing.getOutputs(VanillaTypes.ITEM);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();

		gui.init(0, true, 54 - u, 35 - v - 1);
		gui.init(1, false, 115 - u, 35 - v - 1);

		gui.set(0, inputs.get(0));
		gui.set(1, outputs.get(0));
	}

	@Override
	public Class<? extends BrickMakerRecipeWrapper> getRecipeClass()
	{
		return BrickMakerRecipeWrapper.class;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(BrickMakerRecipeWrapper recipe, IIngredients ingredients)
	{
		recipe.fillIngredients(ingredients);
	}

	@Override
	public void draw(BrickMakerRecipeWrapper recipe, PoseStack stack, double mouseX, double mouseY)
	{
		IRecipeCategory.super.draw(recipe, stack, mouseX, mouseY);
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
	}
}
