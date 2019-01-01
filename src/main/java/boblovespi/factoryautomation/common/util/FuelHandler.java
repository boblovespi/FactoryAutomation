package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.init.Items;

/**
 * Created by Willi on 10/28/2018.
 */
public class FuelHandler
{
	public static void RegisterFuels()
	{
		FuelRegistry.PutInfo(Items.COAL, 2172, 1600, 3520000);
		FuelRegistry.PutInfo(FAItems.coalCoke.ToItem(), 2400, 2000, 5300000);
		FuelRegistry.PutInfo(Items.COAL, 1, 2012, 1600, 2300000);
	}
}
