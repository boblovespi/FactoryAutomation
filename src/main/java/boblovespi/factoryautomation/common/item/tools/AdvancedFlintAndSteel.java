package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;

import net.minecraft.world.item.Item.Properties;

/**
 * Created by Willi on 5/10/2018.
 */
public class AdvancedFlintAndSteel extends FlintAndSteelItem implements FAItem
{
	public AdvancedFlintAndSteel()
	{
		super(new Properties().tab(FAItemGroups.tools).durability(500));
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