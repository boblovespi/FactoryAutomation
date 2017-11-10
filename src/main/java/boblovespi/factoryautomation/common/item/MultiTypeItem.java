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
	private Class<T> itemTypes;

	public MultiTypeItem(String unlocalizedName, CreativeTabs creativeTab,
			Class<T> types)
	{
		super(unlocalizedName, creativeTab);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setHasSubtypes(true);
		itemTypes = types;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		T[] types = itemTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (meta == i)
				return getUnlocalizedName() + "_" + types[i].getName();

		return getUnlocalizedName() + "_" + types[0].getName();
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
