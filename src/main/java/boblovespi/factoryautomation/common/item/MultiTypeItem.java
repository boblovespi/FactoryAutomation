package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 11/9/2017.
 */
public class MultiTypeItem<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends Item implements FAItem
{
	protected final Class<T> itemTypes;
	private final String name;
	private final CreativeTabs creativeTab;
	private final String resourceFolder;
	protected final FABaseItem[] items;

	public MultiTypeItem(String unlocalizedName, CreativeTabs creativeTab, Class<T> types, String resourceFolder)
	{
		this.name = unlocalizedName;
		this.creativeTab = creativeTab;
		itemTypes = types;
		this.resourceFolder = resourceFolder;

		items = new FABaseItem[itemTypes.getEnumConstants().length];

		for (int i = 0; i < items.length; i++)
		{
			items[i] = new FABaseItem(RegistryName() + "_" + itemTypes.getEnumConstants()[i].getName(), creativeTab)
			{
				@Override
				public String GetMetaFilePath(int meta)
				{
					String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";
					return folder + RegistryName();
				}
			};
		}
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		String folder = (resourceFolder == null || resourceFolder.isEmpty()) ? "" : resourceFolder + "/";

		T[] types = itemTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return folder + UnlocalizedName() + "_" + types[i].getName();

		return folder + UnlocalizedName() + "_" + types[0].getName();
	}

	@Override
	public Item ToItem() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("MultiTypeItem is not meant to be an actual item class");
	}

	public FABaseItem GetItem(T type)
	{
		return items[type.GetId()];
	}
}
