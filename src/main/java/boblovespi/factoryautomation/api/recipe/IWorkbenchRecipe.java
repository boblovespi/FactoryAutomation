package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Willi on 4/10/2018.
 */
public interface IWorkbenchRecipe extends IForgeRegistryEntry<IWorkbenchRecipe>
{
	boolean CanFitTier(int x, int y, int tier);

	HashMap<WorkbenchTool.Instance, Integer> GetToolDurabilityUsage();

	HashMap<WorkbenchPart.Instance, Integer> GetPartUsage();

	boolean Matches(IItemHandler workbenchInv, boolean is3x3, int toolIndex, int partIndex, int gridIndex);

	ItemStack GetResult(IItemHandler workbenchInv);

	List<Ingredient> GetItems();

	ItemStack GetResultItem();
}
