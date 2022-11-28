package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * Created by Willi on 11/9/2017.
 */
public class MultiTypeItem<T extends Enum<T> & IMultiTypeEnum & StringRepresentable>
{
	protected final Class<T> itemTypes;
	protected final FABaseItem[] items;
	private final String name;
	private final String resourceFolder;

	public MultiTypeItem(String name, Class<T> types, String resourceFolder, Function<T, Item.Properties> properties)
	{
		this(name, types, resourceFolder, properties, 0L);
	}

	public MultiTypeItem(String name, Class<T> types, String resourceFolder, Function<T, Item.Properties> properties, long ignore)
	{
		this.name = name;
		itemTypes = types;
		this.resourceFolder = resourceFolder;

		items = new FABaseItem[itemTypes.getEnumConstants().length];

		for (int i = 0; i < items.length; i++)
		{
			if ((1L << i & ignore) != 0)
				continue;
			items[i] = new FABaseItem(name + "_" + itemTypes.getEnumConstants()[i].getSerializedName(),
									  properties.apply(itemTypes.getEnumConstants()[i]));
		}
	}

	public MultiTypeItem(String name, Class<T> types, String resourceFolder, Item.Properties properties)
	{
		this(name, types, resourceFolder, n -> properties);
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
}
