package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

/**
 * Created by Willi on 11/9/2017.
 */
public class MultiTypeItem<T extends Enum<T> & IMultiTypeEnum & IStringSerializable>
		extends FABaseItem implements FAItem
{
	protected Class<T> itemTypes;
	private String resourceFolder;

	public MultiTypeItem(String unlocalizedName, CreativeTabs creativeTab,
			Class<T> types, String resourceFolder)
	{
		super(unlocalizedName, creativeTab);
		setHasSubtypes(true);
		itemTypes = types;
		this.resourceFolder = resourceFolder;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		String folder = (resourceFolder == null || resourceFolder.isEmpty()) ?
				"" :
				resourceFolder + "/";

		T[] types = itemTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return folder + UnlocalizedName() + "_" + types[i].getName();

		return folder + UnlocalizedName() + "_" + types[0].getName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		for (int i = 0; i < itemTypes.getEnumConstants().length; i++)
		{
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		T[] types = itemTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (stack.getItemDamage() == i)
				return getUnlocalizedName() + "." + types[i].getName();

		return getUnlocalizedName() + "." + types[0].getName();
	}
}
