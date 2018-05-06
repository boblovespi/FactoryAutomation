package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import boblovespi.factoryautomation.common.util.jei.wrappers.WorkbenchRecipeWrapper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

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
	}

	@Override
	public void register(IModRegistry registry)
	{
		registry.addRecipeCatalyst(new ItemStack(FABlocks.stoneWorkbench.ToBlock()), WORKBENCH);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.ironWorkbench.ToBlock()), WORKBENCH);
		registry.addRecipes(Collections.singletonList(new BlastFurnaceRecipeWrapper()),
				"factoryautomation.blast_furnace");

		registry.addRecipes(WorkbenchRecipeHandler.recipes.values(), WORKBENCH);

		registry.handleRecipes(IWorkbenchRecipe.class, WorkbenchRecipeWrapper::new, WORKBENCH);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{

	}

}
