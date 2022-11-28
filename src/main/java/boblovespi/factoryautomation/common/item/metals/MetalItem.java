package boblovespi.factoryautomation.common.item.metals;

import boblovespi.factoryautomation.common.item.MultiTypeItem;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;

/**
 * Created by Willi on 11/9/2017.
 */
public class MetalItem extends MultiTypeItem<Metals>
{
	public MetalItem(String name, long ignore)
	{
		super(name, Metals.class, "metals", n -> new Item.Properties().tab(FAItemGroups.metallurgy), ignore);
	}

	public MetalItem(String name)
	{
		super(name, Metals.class, "metals", new Item.Properties().tab(FAItemGroups.metallurgy));
	}
}
