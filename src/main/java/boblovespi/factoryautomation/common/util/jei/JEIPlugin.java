package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.JawCrusherRecipe;
import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.categories.BlastFurnaceRecipeCategory;
import boblovespi.factoryautomation.common.util.jei.categories.JawCrusherRecipeCategory;
import boblovespi.factoryautomation.common.util.jei.categories.SteelmakingRecipeCategory;
import boblovespi.factoryautomation.common.util.jei.categories.WorkbenchRecipeCategory;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import boblovespi.factoryautomation.common.util.jei.wrappers.JawCrusherRecipeWrapper;
import boblovespi.factoryautomation.common.util.jei.wrappers.SteelmakingRecipeWrapper;
import boblovespi.factoryautomation.common.util.jei.wrappers.WorkbenchRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Willi on 12/23/2017.
 */

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin
{
	public static final String WORKBENCH = "factoryautomation.workbench";

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{

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
	}

	@Override
	public void register(IModRegistry registry)
	{
		registry.addRecipeCatalyst(new ItemStack(FABlocks.stoneWorkbench.ToBlock()), WORKBENCH);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.ironWorkbench.ToBlock()), WORKBENCH);
		registry.addRecipeCatalyst(
				new ItemStack(FABlocks.blastFurnaceController.ToBlock()), "factoryautomation.blast_furnace");
		registry.addRecipeCatalyst(new ItemStack(FABlocks.jawCrusher.ToBlock()), JawCrusherRecipeCategory.ID);
		registry.addRecipeCatalyst(
				new ItemStack(FABlocks.steelmakingFurnaceController.ToBlock()), SteelmakingRecipeCategory.ID);
		registry.addRecipes(Collections.singletonList(new BlastFurnaceRecipeWrapper()),
				"factoryautomation.blast_furnace");

		registry.addRecipes(WorkbenchRecipeHandler.recipes.values(), WORKBENCH);
		registry.addRecipes(SteelmakingRecipe.GetRecipes(), SteelmakingRecipeCategory.ID);
		registry.addRecipes(JawCrusherRecipe.GetRecipes(), JawCrusherRecipeCategory.ID);

		registry.handleRecipes(IWorkbenchRecipe.class, WorkbenchRecipeWrapper::new, WORKBENCH);
		registry.handleRecipes(SteelmakingRecipe.class, SteelmakingRecipeWrapper::new, SteelmakingRecipeCategory.ID);
		registry.handleRecipes(JawCrusherRecipe.class, JawCrusherRecipeWrapper::new, JawCrusherRecipeCategory.ID);

		RegisterDescriptions(registry);
	}

	private void RegisterDescriptions(IModRegistry registry)
	{
		registry.addIngredientInfo(
				Arrays.asList(new OreIngredient("logWood").getMatchingStacks()), VanillaTypes.ITEM,
				"factoryautomation.jei.logs");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{

	}

}
