package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class FAHoe extends HoeItem
{
	public FAHoe(Tier material, String name, float attackSpeed)
	{
		super(material, (int) -(material.getAttackDamageBonus()), attackSpeed, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		FAItems.items.add(RegistryObjectWrapper.Item(name, this));
	}
}
