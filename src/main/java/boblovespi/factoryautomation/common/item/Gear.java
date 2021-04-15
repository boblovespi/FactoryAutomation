package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.util.FAItemGroups;

/**
 * Created by Willi on 5/6/2018.
 */
public class Gear extends MultiTypeItem<Gearbox.GearType>
{
	public Gear()
	{
		super("gear", Gearbox.GearType.class, "mechanical",
				n -> new Properties().durability(n.durability).tab(FAItemGroups.mechanical));
	}
}
