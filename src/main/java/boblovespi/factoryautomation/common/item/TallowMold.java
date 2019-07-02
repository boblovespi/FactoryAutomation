package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.TallowForms;
import boblovespi.factoryautomation.common.util.FACreativeTabs;

/**
 * Created by Willi on 7/1/2019.
 */
public class TallowMold extends MultiTypeItem<TallowForms>
{
	public TallowMold()
	{
		super("pig_tallow", FACreativeTabs.metallurgy, TallowForms.class, "molds");
	}
}
