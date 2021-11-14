package boblovespi.factoryautomation.common.util;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundHandler
{
	// Rock
	public static SoundEvent breakRock = CreateEvent("block.rock.break");
	public static SoundType rock = new SoundType(1f, 1f, breakRock, SoundEvents.STONE_STEP,
			SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);

	// factory disc
	public static SoundEvent factoryDisc = CreateEvent("music_disc.factory");
	// meter
	public static SoundEvent meterDisc = CreateEvent("music_disc.meter");

	@SubscribeEvent
	public static void RegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(breakRock);
		event.getRegistry().register(factoryDisc);
		event.getRegistry().register(meterDisc);
	}

	public static SoundEvent CreateEvent(String id)
	{
		SoundEvent event = new SoundEvent(new ResourceLocation("factoryautomation", id));
		event.setRegistryName(id);
		return event;
	}
}
