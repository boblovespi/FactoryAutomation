package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.recipe.*;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.jei.categories.*;
import boblovespi.factoryautomation.common.util.jei.wrappers.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
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
		registry.addRecipes(Minecraft.getInstance().world.getRecipeManager()
														 .getRecipes(WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE)
														 .values(), WorkbenchRecipeCategory.ID);
		registry.addRecipes(SteelmakingRecipe.GetRecipes(), SteelmakingRecipeCategory.ID);
		registry.addRecipes(JawCrusherRecipe.GetRecipes(), JawCrusherRecipeCategory.ID);
		registry.addRecipes(ChoppingBlockRecipe.GetRecipes(), ChoppingBlockRecipeCategory.ID);
		RegisterDescriptions(registry);
	}

	private void RegisterDescriptions(IRecipeRegistration registry)
	{
		registry.addIngredientInfo(
				ItemTags.LOGS.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList()),
				VanillaTypes.ITEM, "factoryautomation.jei.logs");
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
