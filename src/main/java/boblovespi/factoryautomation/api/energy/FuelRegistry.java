package boblovespi.factoryautomation.api.energy;

import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * Created by Willi on 10/28/2018.
 */
public class FuelRegistry
{
	public static final FuelInfo NULL = new FuelInfo(0, 0, 0);
	private static final HashMap<Item, FuelInfo> fuels = new HashMap<>(10);

	private FuelRegistry()
	{
	}

	/**
	 * metadata officially deprecated, ItemInfo class useless
	 */
	@Deprecated
	public static FuelInfo getInfo(Item item, int meta)
	{
		return fuels.getOrDefault(item, NULL);
	}

	public static FuelInfo getInfo(Item item)
	{
		return fuels.getOrDefault(item, NULL);
	}

	public static void putInfo(Item item, int meta, float burnTemp, int burnTime, int totalEnergy)
	{
		fuels.put(item, new FuelInfo(burnTemp, burnTime, totalEnergy));
	}

	public static void putInfo(Item item, float burnTemp, int burnTime, int totalEnergy)
	{
		putInfo(item, 0, burnTemp, burnTime, totalEnergy);
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

		public float getBurnTemp()
		{
			return burnTemp;
		}

		public int getBurnTime()
		{
			return burnTime;
		}

		public int getTotalEnergy()
		{
			return totalEnergy;
		}
	}
}
