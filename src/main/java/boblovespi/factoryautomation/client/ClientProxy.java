package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.common.CommonProxy;
import boblovespi.factoryautomation.common.item.FAItems;

/**
 * Created by Willi on 11/8/2017.
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void RegisterRenders()
	{
		super.RegisterRenders();
		FAItems.RegisterRenders();
	}
}
