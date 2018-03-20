package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.api.IUpdatable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 1/10/2018.
 */
public class WorldTickHandler
{
	private static WorldTickHandler instance;
	private List<IUpdatable> handlers = new ArrayList<>(1);

	private WorldTickHandler()
	{
	}

	public static WorldTickHandler GetInstance()
	{
		return instance == null ? instance = new WorldTickHandler() : instance;
	}

	public void AddHandler(IUpdatable handler)
	{
		if (!handlers.contains(handler))
			handlers.add(handler);
	}

	@SubscribeEvent
	public void OnWorldTick(TickEvent.WorldTickEvent tickEvent)
	{
		if (tickEvent.phase == TickEvent.Phase.START
				|| tickEvent.world.isRemote)
			return;
		handlers.forEach(IUpdatable::Update);
	}
}
