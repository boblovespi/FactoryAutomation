package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;

/**
 * Created by Willi on 4/11/2018.
 */
public class ShapedWorkbenchRecipe extends IForgeRegistryEntry.Impl<IWorkbenchRecipe> implements IWorkbenchRecipe
{
	private final int tier;
	private final int sizeX;
	private final int sizeY;
	private final Ingredient[][] recipe;
	private final HashMap<WorkbenchTool.Instance, Integer> tools;
	private final HashMap<WorkbenchPart.Instance, Integer> parts;
	private final ItemStack result;

	public ShapedWorkbenchRecipe(int tier, Ingredient[][] recipe, HashMap<WorkbenchTool.Instance, Integer> tools,
			HashMap<WorkbenchPart.Instance, Integer> parts, ItemStack result)
	{
		this.tier = tier;
		this.sizeX = recipe[0].length;
		this.sizeY = recipe.length;
		this.recipe = recipe;
		this.tools = tools;
		this.parts = parts;
		this.result = result;
	}

	@Override
	public boolean CanFitTier(int x, int y, int tier)
	{
		return tier == this.tier && x <= sizeX && y <= sizeY;
	}

	@Override
	public HashMap<WorkbenchTool.Instance, Integer> GetToolDurabilityUsage()
	{
		return tools;
	}

	@Override
	public HashMap<WorkbenchPart.Instance, Integer> GetPartUsage()
	{
		return parts;
	}

	@Override
	public boolean Matches(IItemHandler workbenchInv, boolean is3x3, int toolIndex, int partIndex, int gridIndex)
	{
		if (is3x3)
		{
			if (sizeX > 3 || sizeY > 3)
				return false;

			for (int x = 0; x < sizeX; x++)
			{
				for (int y = 0; y < sizeY; y++)
				{
					if (!recipe[y][x].apply(workbenchInv.getStackInSlot(gridIndex + x * 3 + y)))
						return false;
				}
			}

			// TODO: finish
		} else
		{

		}
		return true;
	}

	@Override
	public ItemStack GetResult(IItemHandler workbenchInv)
	{
		return result.copy();
	}
}
