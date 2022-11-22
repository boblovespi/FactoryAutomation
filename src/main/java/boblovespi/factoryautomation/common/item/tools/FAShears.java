package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;

import net.minecraft.world.item.Item.Properties;

public class FAShears extends ShearsItem implements FAItem
{
	private String name;

	public FAShears(String name, Properties properties)
	{
		super(properties);
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return UnlocalizedName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
