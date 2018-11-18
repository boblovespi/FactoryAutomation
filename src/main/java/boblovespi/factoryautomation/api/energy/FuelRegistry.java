package boblovespi.factoryautomation.api.energy;

import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * Created by Willi on 10/28/2018.
 */
public class FuelRegistry
{
	public static final FuelInfo NULL = new FuelInfo(0, 0, 0);
	private static final HashMap<ItemInfo, FuelInfo> fuels = new HashMap<>(10);

	private FuelRegistry()
	{
	}

	public static FuelInfo GetInfo(Item item, int meta)
	{
		return fuels.getOrDefault(new ItemInfo(item, meta), NULL);
	}

	public static FuelInfo GetInfo(Item item)
	{
		return GetInfo(item, 0);
	}

	public static void PutInfo(Item item, int meta, float burnTemp, int burnTime, int totalEnergy)
	{
		fuels.put(new ItemInfo(item, meta), new FuelInfo(burnTemp, burnTime, totalEnergy));
	}

	public static void PutInfo(Item item, float burnTemp, int burnTime, int totalEnergy)
	{
		PutInfo(item, 0, burnTemp, burnTime, totalEnergy);
	}

	public static class FuelInfo
	{
		private final float burnTemp;
		private final int burnTime;
		private final int totalEnergy;

		public FuelInfo(float burnTemp, int burnTime, int totalEnergy)
		{
			this.burnTemp = burnTemp;
			this.burnTime = burnTime;
			this.totalEnergy = totalEnergy;
		}

		public float GetBurnTemp()
		{
			return burnTemp;
		}

		public int GetBurnTime()
		{
			return burnTime;
		}

		public int GetTotalEnergy()
		{
			return totalEnergy;
		}
	}

	private static class ItemInfo
	{
		private Item item;
		private int meta;

		private ItemInfo(Item item, int meta)
		{
			this.item = item;
			this.meta = meta;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof ItemInfo)
				if (((ItemInfo) obj).item == item && ((ItemInfo) obj).meta == meta)
					return true;
			return false;
		}

		@Override
		public int hashCode()
		{
			int hash = 1;
			hash = hash * 17 + item.hashCode();
			hash = hash * 31 + meta;
			return hash;
		}
	}
}