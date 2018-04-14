package boblovespi.factoryautomation.api.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Willi on 4/10/2018.
 */
public class WorkbenchTool
{
	public static final WorkbenchTool HAMMER = new WorkbenchTool(new HashMap<ItemStack, Integer>()
	{{
		put(new ItemStack(Items.STONE_HOE), 1);
	}});

	private HashMap<ItemStack, Integer> items;

	public WorkbenchTool(HashMap<ItemStack, Integer> items)
	{
		this.items = items;
	}

	public HashMap<ItemStack, Integer> GetItems()
	{
		return items;
	}

	public void AddItem(ItemStack item, int tier)
	{
		items.put(item, tier);
	}
}
