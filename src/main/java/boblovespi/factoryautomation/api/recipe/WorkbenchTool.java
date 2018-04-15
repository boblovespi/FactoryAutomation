package boblovespi.factoryautomation.api.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * Created by Willi on 4/10/2018.
 */
public class WorkbenchTool
{
	public static final HashMap<ResourceLocation, WorkbenchTool> tools = new HashMap<>(5);
	public static final WorkbenchTool HAMMER = new WorkbenchTool(new ResourceLocation("factoryautomation", "hammer"),
			new HashMap<ItemStack, Integer>()
			{{
				put(new ItemStack(Items.STONE_HOE), 1);
			}});

	private HashMap<ItemStack, Integer> items;

	public WorkbenchTool(ResourceLocation id, HashMap<ItemStack, Integer> items)
	{
		this.items = items;
		tools.put(id, this);
	}

	public HashMap<ItemStack, Integer> GetItems()
	{
		return items;
	}

	public void AddItem(ItemStack item, int tier)
	{
		items.put(item, tier);
	}

	public static class Instance
	{
		public int tier;
		private WorkbenchTool tool;

		private Instance()
		{
			tier = 0;
			tool = null;
		}

		public static Instance FromTool(WorkbenchTool tool, int tier)
		{
			Instance instance = new Instance();
			instance.tool = tool;
			instance.tier = tier;
			return instance;
		}

		public static Instance FromToolStack(ItemStack stack)
		{
			if (stack.isEmpty())
				return null;
			for (WorkbenchTool tool : tools.values())
			{
				if (tool.GetItems().containsKey(stack))
				{
					return FromTool(tool, tool.GetItems().get(stack));
				}
			}

			return null;
		}

		public WorkbenchTool GetTool()
		{
			return tool;
		}

		public boolean IsSameTool(Instance other)
		{
			return other != null && other.GetTool().equals(GetTool());
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
				return false;
			if (!(obj instanceof Instance))
				return false;
			else
				return ((Instance) obj).tier == tier && ((Instance) obj).GetTool().equals(GetTool());
		}
	}
}
