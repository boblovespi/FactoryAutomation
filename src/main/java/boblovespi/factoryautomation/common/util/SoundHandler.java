package boblovespi.factoryautomation.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundHandler
{
	// Rock
	public static RegistryObjectWrapper<SoundEvent> breakRock = RegistryObjectWrapper.Of("block.rock.break",
			SoundHandler::CreateEvent);
	public static SoundType rock = new SoundType(1f, 1f, breakRock.obj(), SoundEvents.STONE_STEP,
			SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);

	// factory disc
	public static RegistryObjectWrapper<SoundEvent> factoryDisc = RegistryObjectWrapper.Of("music_disc.factory",
			SoundHandler::CreateEvent);
	// meter
	public static RegistryObjectWrapper<SoundEvent> meterDisc = RegistryObjectWrapper.Of("music_disc.meter",
			SoundHandler::CreateEvent);

	@SubscribeEvent
	public static void RegisterSounds(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.SOUND_EVENTS, r -> {
			breakRock.Register(r);
			factoryDisc.Register(r);
			meterDisc.Register(r);
		});
	}

	public static SoundEvent CreateEvent(String id)
	{
		return new SoundEvent(new ResourceLocation("factoryautomation", id));
	}
}
