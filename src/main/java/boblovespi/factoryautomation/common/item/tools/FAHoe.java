package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Tier;

public class FAHoe extends HoeItem implements FAItem
{
	private final String unlocalizedName;

	public FAHoe(Tier material, String name, float attackSpeed)
	{
		super(material, (int) -(material.getAttackDamageBonus()), attackSpeed, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		unlocalizedName = name;
		// setUnlocalizedName(name);
		setRegistryName(name);
		FAItems.items.add(this);

	}

	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "tools/" + UnlocalizedName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
