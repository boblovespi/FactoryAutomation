package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

import java.util.function.Consumer;

/**
 * Created by Willi on 11/9/2017.
 */
public class MultiTypeItem<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends Item implements FAItem
{
	protected final Class<T> itemTypes;
	protected final FABaseItem[] items;
	private final String name;
	private final String resourceFolder;

	public MultiTypeItem(String unlocalizedName, Class<T> types, String resourceFolder, Properties properties)
	{
		super(properties);
		this.name = unlocalizedName;
		itemTypes = types;
		this.resourceFolder = resourceFolder;

		items = new FABaseItem[itemTypes.getEnumConstants().length];

		for (int i = 0; i < items.length; i++)
		{
			items[i] = new FABaseItem(RegistryName() + "_" + itemTypes.getEnumConstants()[i].getName(), properties)
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

	public boolean Contains(Item item)
	{
		for (FABaseItem item1 : items)
		{
			if (item1 == item)
				return true;
		}
		return false;
	}

	@Override
	public FAItem Init(Consumer<Item> apply)
	{
		for (FABaseItem item : items)
		{
			item.Init(apply);
		}
		return this;
	}
}
