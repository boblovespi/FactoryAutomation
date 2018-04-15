package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * Created by Willi on 4/10/2018.
 */
public class WorkbenchPart
{
	public static final HashMap<ResourceLocation, WorkbenchPart> parts = new HashMap<>(5);
	public static final WorkbenchPart SCREW = new WorkbenchPart(
			new ResourceLocation("factoryautomation", "screw"), new HashMap<ItemStack, Integer>()
	{{
		// put(new ItemStack(Items.STONE_HOE), 1);
	}});

	private HashMap<ItemStack, Integer> items;

	public WorkbenchPart(ResourceLocation id, HashMap<ItemStack, Integer> items)
	{
		this.items = items;
		parts.putIfAbsent(id, this);
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
		private WorkbenchPart part;

		private Instance()
		{
			tier = 0;
			part = null;
		}

		public static WorkbenchPart.Instance FromPart(WorkbenchPart part, int tier)
		{
			WorkbenchPart.Instance instance = new WorkbenchPart.Instance();
			instance.part = part;
			instance.tier = tier;
			return instance;
		}

		public static Instance FromPartStack(ItemStack stack)
		{
			if (stack.isEmpty())
				return null;
			for (WorkbenchPart part : parts.values())
			{
				if (part.GetItems().containsKey(stack))
				{
					return FromPart(part, part.GetItems().get(stack));
				}
			}

			return null;
		}

		public WorkbenchPart GetPart()
		{
			return part;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
				return false;
			if (!(obj instanceof WorkbenchPart.Instance))
				return false;
			else
				return ((WorkbenchPart.Instance) obj).tier == tier && ((WorkbenchPart.Instance) obj).GetPart()
																									.equals(GetPart());
		}

		public boolean IsSamePart(Instance other)
		{
			return other != null && other.GetPart().equals(GetPart());
		}
	}
}
