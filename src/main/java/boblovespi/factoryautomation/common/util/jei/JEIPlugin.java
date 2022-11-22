package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.api.recipe.*;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.jei.categories.*;
import boblovespi.factoryautomation.common.util.jei.wrappers.BlastFurnaceRecipeWrapper;
import boblovespi.factoryautomation.common.util.jei.wrappers.BrickMakerRecipeWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Collectors;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/23/2017.
 */

@mezz.jei.api.JeiPlugin
public class JEIPlugin implements IModPlugin
{
	private static final ResourceLocation PLUGIN_ID = new ResourceLocation(MODID, "jei_plugin");

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
		registry.addRecipeCategories(new BrickMakerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new MillstoneRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new TumblingBarrelRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry)
	{
		registry.addRecipes(BlastFurnaceRecipeCategory.TYPE, Collections.singletonList(new BlastFurnaceRecipeWrapper()));
		registry.addRecipes(WorkbenchRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
														 .getAllRecipesFor(WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE));
		registry.addRecipes(SteelmakingRecipeCategory.TYPE, SteelmakingRecipe.GetRecipes().stream().toList());
		registry.addRecipes(JawCrusherRecipeCategory.TYPE, JawCrusherRecipe.GetRecipes().stream().toList());
		registry.addRecipes(ChoppingBlockRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ChoppingBlockRecipe.TYPE));
		registry.addRecipes(BrickMakerRecipeCategory.TYPE, Collections.singletonList(new BrickMakerRecipeWrapper()));
		registry.addRecipes(MillstoneRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(MillstoneRecipe.TYPE));
		registry.addRecipes(TumblingBarrelRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TumblingBarrelRecipe.TYPE));
		RegisterDescriptions(registry);
	}

	private void RegisterDescriptions(IRecipeRegistration registry)
	{
		registry.addIngredientInfo(
				ForgeRegistries.ITEMS.tags().getTag(ItemTags.LOGS).stream().map(ItemStack::new).collect(Collectors.toList()),
				VanillaTypes.ITEM_STACK, Component.translatable("factoryautomation.jei.logs"));
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{

	}

	@Override
	public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry)
	{
		registry.addRecipeCatalyst(new ItemStack(FABlocks.stoneWorkbench), WorkbenchRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.ironWorkbench), WorkbenchRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.blastFurnaceController), BlastFurnaceRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.jawCrusher), JawCrusherRecipeCategory.TYPE);
		registry.addRecipeCatalyst(
				new ItemStack(FABlocks.steelmakingFurnaceController), SteelmakingRecipeCategory.TYPE);
		for (Block choppingBlock : FABlocks.woodChoppingBlocks)
		{
			registry.addRecipeCatalyst(new ItemStack(choppingBlock), ChoppingBlockRecipeCategory.TYPE);
		}
		registry.addRecipeCatalyst(new ItemStack(FABlocks.brickMakerFrame), BrickMakerRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.millstone), MillstoneRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(FABlocks.tumblingBarrel), TumblingBarrelRecipeCategory.TYPE);
	}
}
