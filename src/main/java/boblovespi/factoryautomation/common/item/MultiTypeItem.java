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
public class MultiTypeItem<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends FABaseItem
		implements FAItem
{
		public MultiTypeItem(String unlocalizedName, CreativeTabs creativeTab)
		{
			super(unlocalizedName, creativeTab);
			setUnlocalizedName(UnlocalizedName());
			setRegistryName(RegistryName());
			setHasSubtypes(true);
		}

		@Override
		public String UnlocalizedName()
		{
			return "ingot";
		}

		@Override
		public String GetMetaFilePath(int meta)
		{
			return null;
		}

		@Override
		public Item ToItem()
		{
			return this;
		}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{

	}
}
