package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class FASword extends SwordItem
{
	public FASword(Tier material, String name)
	{
		super(material, 3, -2.4f, new Properties().tab(CreativeModeTab.TAB_COMBAT));
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
