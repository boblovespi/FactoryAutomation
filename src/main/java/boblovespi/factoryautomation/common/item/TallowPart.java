package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.item.types.TallowForms;

/**
 * Created by Willi on 7/1/2019.
 */
public class TallowPart extends MultiTypeItem<TallowForms>
{
	public TallowPart()
	{
		super("pig_tallow", TallowForms.class, "molds");
	}
}
