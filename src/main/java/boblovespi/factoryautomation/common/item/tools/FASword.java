package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.SwordItem;

import net.minecraft.world.item.Item.Properties;

public class FASword extends SwordItem implements FAItem
{
	private final String unlocalizedName;

	public FASword(ToolMaterial material, String name)
	{
		super(material, 3, -2.4f, new Properties().tab(CreativeModeTab.TAB_COMBAT));
		unlocalizedName = name;
		// setUnlocalizedName(unlocalizedName);
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
