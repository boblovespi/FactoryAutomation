package boblovespi.factoryautomation.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by Willi on 11/9/2017.
 */
public class Ingot extends MetalItem
{
	public Ingot()
	{
		super("ingot");
	}

	protected Ingot(String unlocalizedName)
	{
		super(unlocalizedName);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		if (meta > 1 && meta < itemTypes.getEnumConstants().length)
			return super.GetMetaFilePath(meta);
		else
			return super.GetMetaFilePath(2);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		for (int i = 2; i < itemTypes.getEnumConstants().length; i++)
		{
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		int meta = stack.getItemDamage();
		if (!(meta > 1) && (meta < itemTypes.getEnumConstants().length))
		{
			ItemStack clone = new ItemStack(stack.getItem(), stack.getCount(),
					2);
			return super.getUnlocalizedName(clone);
		}
		return super.getUnlocalizedName(stack);
	}
}
