package boblovespi.factoryautomation.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Willi on 4/10/2018.
 */
public interface IWorkbenchRecipe extends IForgeRegistry<IWorkbenchRecipe>, Recipe<Container>
{
	boolean CanFitTier(int x, int y, int tier);

	HashMap<WorkbenchTool.Instance, Integer> GetToolDurabilityUsage();

	HashMap<WorkbenchPart.Instance, Integer> GetPartUsage();

	boolean Matches(IItemHandler workbenchInv, boolean is3x3, int toolIndex, int partIndex, int gridIndex);

	ItemStack GetResult(IItemHandler workbenchInv);

	List<Ingredient> GetJeiRecipe();

	ItemStack GetResultItem();

	@Override
	default boolean matches(Container inv, Level levelIn)
	{
		return false;
	}

	@Override
	default ItemStack assemble(Container inv)
	{
		return GetResultItem();
	}

	@Override
	default boolean canCraftInDimensions(int width, int height)
	{
		return CanFitTier(width, height, 0);
	}

	@Override
	default ItemStack getResultItem()
	{
		return GetResultItem();
	}

	@Override
	default RecipeType<?> getType()
	{
		return WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE;
	}

	@Override
	default ResourceLocation getId()
	{
		return getRegistryName();
	}
}
