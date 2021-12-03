package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.*;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class FAPickaxe extends PickaxeItem implements FAItem
{
	private final String unlocalizedName;

	public FAPickaxe(Tier material, String name)
	{
		super(material, 1, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
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
