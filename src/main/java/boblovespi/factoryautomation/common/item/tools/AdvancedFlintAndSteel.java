package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;

/**
 * Created by Willi on 5/10/2018.
 */
public class AdvancedFlintAndSteel extends FlintAndSteelItem implements FAItem
{
	public AdvancedFlintAndSteel()
	{
		super(new Properties().tab(FAItemGroups.tools).durability(500));
		setRegistryName(registryName());
		FAItems.items.add(this);
	}

	@Override
	public String unlocalizedName()
	{
		return "advanced_flint_and_steel";
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "tools/" + registryName();
	}

	@Override
	public Item toItem()
	{
		return this;
	}
}