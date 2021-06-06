package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class FAHoe extends HoeItem implements FAItem
{
	private final String unlocalizedName;

	public FAHoe(ToolMaterial material, String name, float attackSpeed)
	{
		super(material, (int) -(material.getAttackDamageBonus()), attackSpeed, new Item.Properties().tab(ItemGroup.TAB_TOOLS));
		unlocalizedName = name;
		// setUnlocalizedName(name);
		setRegistryName(name);
		FAItems.items.add(this);

	}

	@Override
	public String unlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "tools/" + unlocalizedName();
	}

	@Override
	public Item toItem()
	{
		return this;
	}
}
