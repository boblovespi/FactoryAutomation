package boblovespi.factoryautomation.common.util;

import net.minecraft.item.Item;

/**
 * Created by Willi on 12/1/2018.
 */
public class ItemInfo
{
	private final Item item;
	private final int meta;

	public ItemInfo(Item item, int meta)
	{
		this.item = item;
		this.meta = meta;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ItemInfo)
			if (((ItemInfo) obj).item == item && ((ItemInfo) obj).meta == meta)
				return true;
		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = hash * 17 + item.hashCode();
		hash = hash * 31 + meta;
		return hash;
	}
}
