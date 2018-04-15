package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.common.worldgen.WorldGenHandler;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Willi on 4/15/2018.
 */
public class ServerProxy implements CommonProxy
{
	@Override
	public void RegisterRenders()
	{

	}

	@Override
	public void PreInit()
	{

	}

	@Override
	public void Init()
	{
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 0);
	}

	@Override
	public void AddChatMessage(ChatType type, TextComponentString string)
	{

	}
}
