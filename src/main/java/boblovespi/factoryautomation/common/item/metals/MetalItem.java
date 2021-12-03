package boblovespi.factoryautomation.common.item.metals;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.MultiTypeItem;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;

/**
 * Created by Willi on 11/9/2017.
 */
public class MetalItem extends MultiTypeItem<Metals> implements FAItem
{
	private String unlocalizedName;

	public MetalItem(String unlocalizedName)
	{
		super(unlocalizedName, Metals.class, "metals", new Item.Properties().tab(FAItemGroups.metallurgy));
	}
}
