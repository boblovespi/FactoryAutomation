package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.TallowForms;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;

/**
 * Created by Willi on 7/1/2019.
 */
public class TallowPart extends MultiTypeItem<TallowForms>
{
	public TallowPart()
	{
		super("pig_tallow", TallowForms.class, "molds", new Item.Properties().tab(FAItemGroups.metallurgy));
	}
}
