package boblovespi.factoryautomation.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Willi on 4/10/2018.
 */
public interface IWorkbenchRecipe extends IForgeRegistryEntry<IWorkbenchRecipe>, IRecipe<IInventory>
{
	boolean CanFitTier(int x, int y, int tier);

	HashMap<WorkbenchTool.Instance, Integer> GetToolDurabilityUsage();

	HashMap<WorkbenchPart.Instance, Integer> GetPartUsage();

	boolean Matches(IItemHandler workbenchInv, boolean is3x3, int toolIndex, int partIndex, int gridIndex);

	ItemStack GetResult(IItemHandler workbenchInv);

	List<Ingredient> GetJeiRecipe();

	ItemStack GetResultItem();

	@Override
	default boolean matches(IInventory inv, World levelIn)
	{
		return false;
	}

	@Override
	default ItemStack getCraftingResult(IInventory inv)
	{
		return GetResultItem();
	}

	@Override
	default boolean canFit(int width, int height)
	{
		return CanFitTier(width, height, 0);
	}

	@Override
	default ItemStack getRecipeOutput()
	{
		return GetResultItem();
	}

	@Override
	default IRecipeType<?> getType()
	{
		return WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE;
	}

	@Override
	default ResourceLocation getId()
	{
		return getRegistryName();
	}
}
