package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.api.IUpdatable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 1/10/2018.
 */
public class ServerTickHandler
{
	private static ServerTickHandler instance;
	private List<IUpdatable> handlers = new ArrayList<>(1);

	private ServerTickHandler()
	{
	}

	public static ServerTickHandler GetInstance()
	{
		return instance == null ? instance = new ServerTickHandler() : instance;
	}

	public void AddHandler(IUpdatable handler)
	{
		if (!handlers.contains(handler))
			handlers.add(handler);
	}

	@Mod.EventHandler
	public void onServerTick(TickEvent.ServerTickEvent tickEvent)
	{
		handlers.forEach(IUpdatable::Update);
	}
}
