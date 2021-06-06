package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class FAAxe extends AxeItem implements FAItem
{
	private final String unlocalizedName;

	public FAAxe(ToolMaterial material, String name)
	{
		super(material, 6, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS));
		unlocalizedName = name;
		// this.setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(FactoryAutomation.MODID, name));
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
