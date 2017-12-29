package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.common.handler.WorldGenHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Willi on 11/8/2017.
 */
public class CommonProxy
{
	public void RegisterRenders()
	{
	}

	public void PreInit()
	{

	}

	public void Init()
	{
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 0);
	}
}
