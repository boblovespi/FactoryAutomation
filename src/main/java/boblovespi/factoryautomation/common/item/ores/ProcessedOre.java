package boblovespi.factoryautomation.common.item.ores;

import boblovespi.factoryautomation.common.item.MultiTypeItem;

/**
 * Created by Willi on 8/12/2018.
 */
public class ProcessedOre extends MultiTypeItem<OreForms>
{
	public ProcessedOre(String name)
	{
		super(name, OreForms.class, "ores", new Properties());
	}
}
