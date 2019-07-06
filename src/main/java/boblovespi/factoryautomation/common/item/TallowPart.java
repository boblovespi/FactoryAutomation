package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.TallowForms;
import boblovespi.factoryautomation.common.util.FACreativeTabs;

/**
 * Created by Willi on 7/1/2019.
 */
public class TallowPart extends MultiTypeItem<TallowForms>
{
	public TallowPart()
	{
		super("pig_tallow", FACreativeTabs.metallurgy, TallowForms.class, "molds");
	}
}
