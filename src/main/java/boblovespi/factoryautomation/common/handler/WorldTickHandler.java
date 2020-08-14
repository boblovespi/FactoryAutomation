package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.api.IUpdatable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 1/10/2018.
 * class for all tick handlers
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
		handlers.forEach(n -> n.Update(tickEvent.world));
	}
}
