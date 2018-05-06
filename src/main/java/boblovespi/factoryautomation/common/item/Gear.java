package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.util.FACreativeTabs;

/**
 * Created by Willi on 5/6/2018.
 */
public class Gear extends MultiTypeItem<Gearbox.GearType>
{
	public Gear()
	{
		super("gear", FACreativeTabs.mechanical, Gearbox.GearType.class, "mechanical");
		for (int i = 0; i < items.length; ++i)
		{
			items[i].setMaxDamage(Gearbox.GearType.values()[i].durability);
		}
	}
}
