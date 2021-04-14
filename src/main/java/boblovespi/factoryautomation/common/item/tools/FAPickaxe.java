package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.*;

public class FAPickaxe extends PickaxeItem implements FAItem
{
	private final String unlocalizedName;

	public FAPickaxe(IItemTier material, String name)
	{
		super(material, 1, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS));
		this.unlocalizedName = name;
		// this.setUnlocalizedName(unlocalizedName); TODO: Translation key??
		this.setRegistryName(name);
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
