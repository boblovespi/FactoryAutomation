package boblovespi.factoryautomation.common.util.jei.categories;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.TumblingBarrelRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class TumblingBarrelRecipeCategory extends AbstractMachineRecipeCategory<TumblingBarrelRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(FactoryAutomation.MODID, "tumbling_barrel");
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
		icon = guiHelper.createDrawableIngredient(new ItemStack(FABlocks.tumblingBarrel));
		tank = guiHelper.createDrawable(new ResourceLocation("factoryautomation:textures/gui/container/tumbling_barrel.png"), 182, 32, 16, 59);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

	@Override
	public Class<? extends TumblingBarrelRecipe> getRecipeClass()
	{
		return TumblingBarrelRecipe.class;
	}

	@Nonnull
	@Override
	public Component getTitle()
	{
		return new TranslatableComponent("gui.tumbling_barrel");
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
	public void setRecipe(IRecipeLayout recipeLayout, TumblingBarrelRecipe recipe, IIngredients ing)
	{
		var inputs = ing.getInputs(VanillaTypes.ITEM);
		var outputs = ing.getOutputs(VanillaTypes.ITEM);
		var fluidIn = ing.getInputs(VanillaTypes.FLUID);
		var fluidOut = ing.getOutputs(VanillaTypes.FLUID);

		IGuiItemStackGroup gui = recipeLayout.getItemStacks();
		var fluids = recipeLayout.getFluidStacks();

		gui.init(0, true, 56 - u, 34 - v - 1);
		gui.init(1, false, 108 - u, 34 - v - 1);

		if (!inputs.get(0).isEmpty() && !inputs.get(0).get(0).isEmpty())
			gui.set(0, inputs.get(0));
		if (!outputs.isEmpty() && !outputs.get(0).isEmpty() && !outputs.get(0).get(0).isEmpty())
			gui.set(1, outputs.get(0));

		fluids.init(0, true, 8 - u, 8 - v, 16, 59, 2000, false, tank);
		fluids.init(1, false, 152 - u, 8 - v, 16, 59, 2000, false, tank);

		if (!fluidIn.get(0).isEmpty() && !fluidIn.get(0).get(0).isEmpty())
			fluids.set(0, fluidIn.get(0));
		if (!fluidOut.get(0).isEmpty() && !fluidOut.get(0).get(0).isEmpty())
			fluids.set(1, fluidOut.get(0));
	}

	@Override
	public void draw(TumblingBarrelRecipe recipe, PoseStack stack, double mouseX, double mouseY)
	{
		super.draw(recipe, stack, mouseX, mouseY);
		var text = new TranslatableComponent("jei.minMaxSpeed", recipe.GetMinSpeed(), recipe.GetMaxSpeed()).withStyle(ChatFormatting.ITALIC)
				.withStyle(ChatFormatting.GRAY);
		Minecraft.getInstance().font.draw(stack, text, 54 - u, 60 - v - 1, 1);
	}
}

