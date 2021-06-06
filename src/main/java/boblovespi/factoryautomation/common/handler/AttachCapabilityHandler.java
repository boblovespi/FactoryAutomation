package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.pollution.PollutionCapabilityProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Willi on 5/20/2018.
 * class for attaching capabilities to stuff
 */

@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID)
public class AttachCapabilityHandler
{
	@SubscribeEvent
	public static void attachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event)
	{
		event.addCapability(
				new ResourceLocation(FactoryAutomation.MODID, "pollution"),
				new PollutionCapabilityProvider(event.getObject()));
	}
}
