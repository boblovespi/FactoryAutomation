package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

public class FASword extends SwordItem implements FAItem
{
	private final String unlocalizedName;

	public FASword(ToolMaterial material, String name)
	{
		super(material, 3, -2.4f, new Properties().tab(ItemGroup.TAB_COMBAT));
		unlocalizedName = name;
		// setUnlocalizedName(unlocalizedName);
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
