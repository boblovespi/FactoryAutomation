package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Willi on 11/9/2017.
 */
public class MetalItem extends MultiTypeItem<Metals>
		implements FAItem
{
	private String unlocalizedName;

	public MetalItem(String unlocalizedName)
	{
		super(unlocalizedName, CreativeTabs.MATERIALS, Metals.class, "metals");
	}
}
