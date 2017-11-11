package boblovespi.factoryautomation.common.item.Tools;

import net.minecraft.item.ItemPickaxe;

public class ItemModPickaxe extends ItemPickaxe{

	public ItemModPickaxe(ToolMaterial material, String unlocalizedName) {
		super(material);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(unlocalizedName);
	}

}
