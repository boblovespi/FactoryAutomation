package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.Items;

/**
 * Created by Willi on 10/28/2018.
 */
public class FuelHandler
{
	public static void registerFuels()
	{
		FuelRegistry.putInfo(Items.COAL, 2172, 1600, 3520000);
		FuelRegistry.putInfo(FAItems.coalCoke.toItem(), 2400, 2000, 5300000);
		FuelRegistry.putInfo(Items.CHARCOAL, 2012, 1600, 2300000);
	}
}
