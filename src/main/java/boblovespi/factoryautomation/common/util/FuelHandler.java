package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.Items;

/**
 * Created by Willi on 10/28/2018.
 */
public class FuelHandler
{
	public static void RegisterFuels()
	{
		FuelRegistry.PutInfo(Items.COAL, 2172, 1600, 4300000000f);
		FuelRegistry.PutInfo(FAItems.coalCoke, 2400, 2000, 7300000000f);
		FuelRegistry.PutInfo(Items.CHARCOAL, 2012, 1600, 4300000000f);
	}
}
