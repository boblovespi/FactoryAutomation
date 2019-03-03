package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.api.recipe.*;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.categories.*;
import boblovespi.factoryautomation.common.util.jei.wrappers.*;
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
		registry.addRecipeCategories(new ChoppingBlockRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
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
		registry.addRecipeCatalyst(new ItemStack(FABlocks.woodChoppingBlock.ToBlock()), ChoppingBlockRecipeCategory.ID);

		registry.addRecipes(Collections.singletonList(new BlastFurnaceRecipeWrapper()),
				"factoryautomation.blast_furnace");
		registry.addRecipes(WorkbenchRecipeHandler.recipes.values(), WORKBENCH);
		registry.addRecipes(SteelmakingRecipe.GetRecipes(), SteelmakingRecipeCategory.ID);
		registry.addRecipes(JawCrusherRecipe.GetRecipes(), JawCrusherRecipeCategory.ID);
		registry.addRecipes(ChoppingBlockRecipe.GetRecipes(), ChoppingBlockRecipeCategory.ID);

		registry.handleRecipes(IWorkbenchRecipe.class, WorkbenchRecipeWrapper::new, WORKBENCH);
		registry.handleRecipes(SteelmakingRecipe.class, SteelmakingRecipeWrapper::new, SteelmakingRecipeCategory.ID);
		registry.handleRecipes(JawCrusherRecipe.class, JawCrusherRecipeWrapper::new, JawCrusherRecipeCategory.ID);
		registry.handleRecipes(
				ChoppingBlockRecipe.class, ChoppingBlockRecipeWrapper::new, ChoppingBlockRecipeCategory.ID);

		RegisterDescriptions(registry);
	}

	private void RegisterDescriptions(IModRegistry registry)
	{
		registry.addIngredientInfo(Arrays.asList(new OreIngredient("logWood").getMatchingStacks()), VanillaTypes.ITEM,
				"factoryautomation.jei.logs");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{

	}

}
