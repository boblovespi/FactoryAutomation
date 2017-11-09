package boblovespi.factoryautomation.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Willi on 4/13/2017.
 * the default item class, all items w/o metadata and special effects should use this
 */

public class FABaseItem extends Item implements FAItem
{
	private String unlocalizedName;

	public FABaseItem(String unlocalizedName, CreativeTabs ct)
	{
		this.unlocalizedName = unlocalizedName;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegisteryName());
		setCreativeTab(ct);

	}

	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
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
