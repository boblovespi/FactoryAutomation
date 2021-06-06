package boblovespi.factoryautomation.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Created by Willi on 4/13/2017.
 * the default item class, all items w/o metadata and special effects should use this
 */

public class FABaseItem extends Item implements FAItem
{
	private final String unlocalizedName;

	public FABaseItem(String unlocalizedName, ItemGroup ct)
	{
		this(unlocalizedName, new Properties().tab(ct));
	}

	public FABaseItem(String unlocalizedName, Properties properties)
	{
		super(properties);
		this.unlocalizedName = unlocalizedName;
		// setUnlocalizedName(UnlocalizedName()); TODO: figure out if we need to override translation key
		setRegistryName(
				registryName() == null ? unlocalizedName() : registryName());
		FAItems.items.add(this);
	}

	@Override
	public String unlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return unlocalizedName();
	}

	@Override
	public Item toItem()
	{
		return this;
	}
}
