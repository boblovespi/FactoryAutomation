package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class FAAxe extends AxeItem
{
	public FAAxe(Tier material, String name)
	{
		super(material, 6, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
