package boblovespi.factoryautomation.common.item.ores;

import boblovespi.factoryautomation.common.item.MultiTypeItem;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.item.Item;

/**
 * Created by Willi on 8/12/2018.
 */
public class ProcessedOre extends MultiTypeItem<OreForms>
{
	public ProcessedOre(String name)
	{
		super(name, OreForms.class, "ores", new Item.Properties().tab(FAItemGroups.resources));
	}
}
