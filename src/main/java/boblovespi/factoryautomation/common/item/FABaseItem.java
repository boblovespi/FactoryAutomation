package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * Created by Willi on 4/13/2017.
 * the default item class, all items w/o metadata and special effects should use this
 */

public class FABaseItem extends Item
{
	public FABaseItem(String name, CreativeModeTab ct)
	{
		this(name, new Properties().tab(ct));
	}

	public FABaseItem(String name, Properties properties)
	{
		super(properties);
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
