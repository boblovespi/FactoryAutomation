package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MillstoneRecipeCategory implements IRecipeCategory<MillstoneRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "millstone");
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;

	public MillstoneRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"),
								u, v, 83, 54);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.millstone));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Override
	public Class<? extends MillstoneRecipe> getRecipeClass()
	{
		return MillstoneRecipe.class;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return new TranslatableComponent("block.factoryautomation.millstone");
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
	public void setRecipe(IRecipeLayout recipeLayout, MillstoneRecipe recipe, IIngredients ing)
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
	public void setIngredients(MillstoneRecipe recipe, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.GetInput().getItems())));
		ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.GetOutputs()));
	}

	@Override
	public void draw(MillstoneRecipe recipe, PoseStack stack, double mouseX, double mouseY)
	{
		IRecipeCategory.super.draw(recipe, stack, mouseX, mouseY);
		var text = new TranslatableComponent("jei.withTorqueAndTime", recipe.GetTorque(), recipe.GetTime()).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
	}
}

