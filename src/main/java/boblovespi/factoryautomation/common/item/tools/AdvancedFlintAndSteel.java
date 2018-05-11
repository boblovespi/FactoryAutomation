package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;

/**
 * Created by Willi on 5/10/2018.
 */
public class AdvancedFlintAndSteel extends ItemFlintAndSteel implements FAItem
{
	public AdvancedFlintAndSteel()
	{
		setCreativeTab(FACreativeTabs.tools);
		setMaxDamage(500);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		FAItems.items.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return "advanced_flint_and_steel";
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "tools/" + RegistryName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}