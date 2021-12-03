package boblovespi.factoryautomation.api.energy;

import net.minecraft.world.item.Item;

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
	public static FuelInfo GetInfo(Item item, int meta)
	{
		return fuels.getOrDefault(item, NULL);
	}

	public static FuelInfo GetInfo(Item item)
	{
		return fuels.getOrDefault(item, NULL);
	}

	public static void PutInfo(Item item, int meta, float burnTemp, int burnTime, float totalEnergy)
	{
		fuels.put(item, new FuelInfo(burnTemp, burnTime, totalEnergy));
	}

	public static void PutInfo(Item item, float burnTemp, int burnTime, float totalEnergy)
	{
		PutInfo(item, 0, burnTemp, burnTime, totalEnergy);
	}

	public static class FuelInfo
	{
		private final float burnTemp;
		private final int burnTime;
		private final float totalEnergy;

		public FuelInfo(float burnTemp, int burnTime, float totalEnergy)
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

		public float GetTotalEnergy()
		{
			return totalEnergy;
		}
	}
}
