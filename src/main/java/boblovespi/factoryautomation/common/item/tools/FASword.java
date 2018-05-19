package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class FASword extends ItemSword implements FAItem{
	private final String unlocalizedName;
	public FASword(ToolMaterial material, String unlocalizedName) {
		super(material);
		this.unlocalizedName = unlocalizedName;
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(unlocalizedName);
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
