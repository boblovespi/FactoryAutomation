package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.resources.ResourceLocation;

public class FAShovel extends ShovelItem implements FAItem
{
	private final String unlocalizedName;

	public FAShovel(ToolMaterial material, String name)
	{
		super(material, 1.5f, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
		unlocalizedName = name;
		// this.setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(FactoryAutomation.MODID, name));
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
