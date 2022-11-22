package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class FAShovel extends ShovelItem
{
	public FAShovel(Tier material, String name)
	{
		super(material, 1.5f, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
