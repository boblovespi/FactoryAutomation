package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.TumblingBarrelRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.forge.ForgeTypes;
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

public class TumblingBarrelRecipeCategory implements IRecipeCategory<TumblingBarrelRecipe>
{
	public static final RecipeType<TumblingBarrelRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID,
			"tumbling_barrel", TumblingBarrelRecipe.class);
	private static final int u = 7;
	private static final int v = 7;
	private final IGuiHelper guiHelper;
	private final IDrawableStatic background;
	private IDrawable icon;
	private IDrawable tank;

	public TumblingBarrelRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
		background = guiHelper
				.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/tumbling_barrel.png"),
						u, v, 162, 61);
		icon = guiHelper.createDrawableItemStack(new ItemStack(FABlocks.tumblingBarrel));
		tank = guiHelper.createDrawable(
				new ResourceLocation("factoryautomation:textures/gui/container/tumbling_barrel.png"), 182, 32, 16, 59);
	}

	@Override
	public RecipeType<TumblingBarrelRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return Component.translatable("gui.tumbling_barrel");
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
	public void setRecipe(IRecipeLayoutBuilder builder, TumblingBarrelRecipe recipe, IFocusGroup focuses)
	{
		var input = builder.addSlot(RecipeIngredientRole.INPUT, 57 - u, 34 - v);
		var output = builder.addSlot(RecipeIngredientRole.OUTPUT, 109 - u, 34 - v);
		var fluidIn = builder.addSlot(RecipeIngredientRole.INPUT, 8 - u, 8 - v)
							 .setFluidRenderer(2000, false, 16, 59).setOverlay(tank, 0, 0);
		var fluidOut = builder.addSlot(RecipeIngredientRole.OUTPUT, 152 - u, 8 - v)
							  .setFluidRenderer(2000, false, 16, 59).setOverlay(tank, 0, 0);

		if (!recipe.GetInput().isEmpty())
			input.addIngredients(recipe.GetInput());
		if (!recipe.GetPrimaryItemOutputs().get(0).isEmpty())
			output.addItemStack(recipe.GetPrimaryItemOutputs().get(0));
		if (!recipe.GetFluidInputs().get(0).isEmpty())
			fluidIn.addIngredient(ForgeTypes.FLUID_STACK, recipe.GetFluidInputs().get(0));
		if (!recipe.GetPrimaryFluidOutputs().get(0).isEmpty())
			fluidOut.addIngredient(ForgeTypes.FLUID_STACK, recipe.GetPrimaryFluidOutputs().get(0));
	}

	@Override
	public void draw(TumblingBarrelRecipe recipe, IRecipeSlotsView view, PoseStack stack, double mouseX, double mouseY)
	{
		IRecipeCategory.super.draw(recipe, view, stack, mouseX, mouseY);
		var text = Component.translatable("jei.min_max_speed", recipe.GetMinSpeed(), recipe.GetMaxSpeed())
							.withStyle(ChatFormatting.DARK_GRAY);
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
	}
}