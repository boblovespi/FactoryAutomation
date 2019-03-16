package boblovespi.factoryautomation.common.util;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SoundHandler
{
	// Rock
	public static SoundEvent breakRock = CreateEvent("break_rock");
	public static SoundType rock = new SoundType(1f, 1f, breakRock, SoundEvents.BLOCK_STONE_STEP,
			SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

	@SubscribeEvent
	public static void RegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(breakRock);
	}

	public static SoundEvent CreateEvent(String id)
	{
		SoundEvent event = new SoundEvent(new ResourceLocation("factoryautomation", id));
		event.setRegistryName(id);
		return event;
	}
}
