package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.FlintAndSteelItem;

/**
 * Created by Willi on 5/10/2018.
 */
public class AdvancedFlintAndSteel extends FlintAndSteelItem
{
	public AdvancedFlintAndSteel()
	{
		super(new Properties().tab(FAItemGroups.tools).durability(500));
		FAItems.items.add(RegistryObjectWrapper.Item("advanced_flint_and_steel", this));
	}
}