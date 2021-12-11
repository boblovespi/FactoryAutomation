package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.categories.*;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by Willi on 12/23/2017.
 */

@mezz.jei.api.JeiPlugin
public class JEIPlugin implements IModPlugin
{
	private static final ResourceLocation PLUGIN_ID = new ResourceLocation(FactoryAutomation.MODID, "jei_plugin");

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry)
	{

	}

	@Nonnull
	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_ID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		registry.addRecipeCategories(new BlastFurnaceRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new WorkbenchRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new JawCrusherRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SteelmakingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ChoppingBlockRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry)
	{
		registry.addRecipes(Collections.singletonList(new BlastFurnaceRecipeWrapper()), BlastFurnaceRecipeCategory.ID);
		registry.addRecipes(Minecraft.getInstance().level.getRecipeManager()
														 .getAllRecipesFor(WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE), WorkbenchRecipeCategory.ID);
		registry.addRecipes(SteelmakingRecipe.GetRecipes(), SteelmakingRecipeCategory.ID);
		registry.addRecipes(JawCrusherRecipe.GetRecipes(), JawCrusherRecipeCategory.ID);
		registry.addRecipes(ChoppingBlockRecipe.GetRecipes(), ChoppingBlockRecipeCategory.ID);
		RegisterDescriptions(registry);
	}

	private void RegisterDescriptions(IRecipeRegistration registry)
	{
		registry.addIngredientInfo(
				ItemTags.LOGS.getValues().stream().map(ItemStack::new).collect(Collectors.toList()),
				VanillaTypes.ITEM, new TranslatableComponent("factoryautomation.jei.logs"));
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{

	}

	@Override
	public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry)
	{
		registry.addRecipeCatalyst(new ItemStack(FABlocks.stoneWorkbench.ToBlock()), WorkbenchRecipeCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.ironWorkbench.ToBlock()), WorkbenchRecipeCategory.ID);
		registry.addRecipeCatalyst(
				new ItemStack(FABlocks.blastFurnaceController.ToBlock()), BlastFurnaceRecipeCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.jawCrusher.ToBlock()), JawCrusherRecipeCategory.ID);
		registry.addRecipeCatalyst(
				new ItemStack(FABlocks.steelmakingFurnaceController.ToBlock()), SteelmakingRecipeCategory.ID);
		for (FABlock choppingBlock : FABlocks.woodChoppingBlocks)
		{
			registry.addRecipeCatalyst(new ItemStack(choppingBlock.ToBlock()), ChoppingBlockRecipeCategory.ID);
		}
	}
}
