package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class FAPickaxe extends PickaxeItem
{
	public FAPickaxe(Tier material, String name)
	{
		super(material, 1, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
