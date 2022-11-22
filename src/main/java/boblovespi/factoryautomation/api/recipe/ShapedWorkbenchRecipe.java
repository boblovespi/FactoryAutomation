package boblovespi.factoryautomation.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 4/11/2018.
 */
public class ShapedWorkbenchRecipe implements IWorkbenchRecipe
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
		return tier >= this.tier && x >= sizeX && y >= sizeY;
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
		}
		int size = is3x3 ? 3 : 5;
		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				if (y < sizeY && x < sizeX)
				{
					if (!recipe[y][x].test(workbenchInv.getStackInSlot(gridIndex + x * size + y))) // correct
						return false;
				} else if (!workbenchInv.getStackInSlot(gridIndex + x * size + y).isEmpty()) // make sure all other slots are empty
					return false;
			}
		}

		for (Map.Entry<WorkbenchTool.Instance, Integer> toolInfo : tools.entrySet())
		{
			boolean isPresent = false;
			for (int i = 0; i < size; i++)
			{
				WorkbenchTool.Instance tool = WorkbenchTool.Instance
						.FromToolStack(workbenchInv.getStackInSlot(i + toolIndex));

				if (toolInfo.getKey().IsSameTool(tool) && toolInfo.getKey().tier <= tool.tier)
				{
					isPresent = true;
					break;
				}
			}
			if (!isPresent)
				return false;
		}

		for (Map.Entry<WorkbenchPart.Instance, Integer> partInfo : parts.entrySet())
		{
			boolean isPresent = false;
			for (int i = 0; i < size; i++)
			{
				WorkbenchPart.Instance part = WorkbenchPart.Instance
						.FromPartStack(workbenchInv.getStackInSlot(i + partIndex));

				if (partInfo.getKey().IsSamePart(part) && partInfo.getKey().tier <= part.tier
						&& workbenchInv.getStackInSlot(i + partIndex).getCount() >= partInfo.getValue())
				{
					isPresent = true;
					break;
				}
			}
			if (!isPresent)
				return false;
		}

		return true;
	}

	@Override
	public ItemStack GetResult(IItemHandler workbenchInv)
	{
		return result.copy();
	}

	@Override
	public List<Ingredient> GetJeiRecipe()
	{
		List<Ingredient> list = new ArrayList<>(25);

		for (int y = 0; y < 5; y++)
		{
			Ingredient[] a;
			a = (y < recipe.length ? recipe[y] : new Ingredient[0]);
			for (int x = 0; x < 5; x++)
			{
				Ingredient ingredient;
				ingredient = (x < a.length ? a[x] : Ingredient.EMPTY);
				list.add(ingredient);
			}
		}

		return list;
	}

	@Override
	public ItemStack GetResultItem()
	{
		return result.copy();
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return WorkbenchRecipeHandler.SHAPED_SERIALIZER;
	}

	int GetTier()
	{
		return tier;
	}

	int GetSizeX()
	{
		return sizeX;
	}

	int GetSizeY()
	{
		return sizeY;
	}

	Ingredient[][] GetRecipe()
	{
		return recipe;
	}
}
