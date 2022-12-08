package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.api.recipe.TripHammerRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class TripHammerRecipeCategory implements IRecipeCategory<TripHammerRecipe>
{
	public static final RecipeType<TripHammerRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "trip_hammer",
			TripHammerRecipe.class);
	private static final int u = 54;
	private static final int v = 16;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;

	public TripHammerRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/generic_energyless.png"), u, v, 83, 54);
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.tripHammerController));
	}

	@Override
	public RecipeType<TripHammerRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("block.factoryautomation.trip_hammer");
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
	public void setRecipe(IRecipeLayoutBuilder builder, TripHammerRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 55 - u, 35 - v).addIngredients(recipe.itemInput);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 116 - u, 35 - v).addItemStacks(List.of(recipe.itemOutput));
	}

	@Override
	public void draw(TripHammerRecipe recipe, IRecipeSlotsView view, PoseStack stack, double mouseX, double mouseY)
	{
		IRecipeCategory.super.draw(recipe, view, stack, mouseX, mouseY);
		var text = Component.translatable("jei.with_torque_and_time", recipe.torque, recipe.time)
							.withStyle(ChatFormatting.DARK_GRAY);
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
	}
}

