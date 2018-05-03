package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.Item;
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
			new HashMap<Item, Integer>()
			{{
				put(FAItems.ironHammer.ToItem(), 1);
				put(FAItems.steelHammer.ToItem(), 2);
			}});
	public static final WorkbenchTool WRENCH = new WorkbenchTool(new ResourceLocation("factoryautomation", "wrench"),
			new HashMap<Item, Integer>()
			{{
				put(FAItems.steelWrench.ToItem(), 2);
			}});
	public static final WorkbenchTool PINCHERS = new WorkbenchTool(
			new ResourceLocation("factoryautomation", "pinchers"), new HashMap<Item, Integer>()
	{{
		put(FAItems.steelPinchers.ToItem(), 2);
	}});

	private HashMap<Item, Integer> items;

	public WorkbenchTool(ResourceLocation id, HashMap<Item, Integer> items)
	{
		this.items = items;
		tools.put(id, this);
	}

	public HashMap<Item, Integer> GetItems()
	{
		return items;
	}

	public void AddItem(Item item, int tier)
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
				if (tool.GetItems().containsKey(stack.getItem()))
				{
					return FromTool(tool, tool.GetItems().get(stack.getItem()));
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
