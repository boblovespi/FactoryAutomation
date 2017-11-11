package boblovespi.factoryautomation.common.item.Tools;

import net.minecraft.item.ItemSpade;

public class ItemModShovel extends ItemSpade{

	public ItemModShovel(ToolMaterial material, String unlocalizedName) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(unlocalizedName);

	}

}
