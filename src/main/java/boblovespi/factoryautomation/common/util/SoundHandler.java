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

	public static final INSTANCE = new SoundHandler();
	
	private SoundHandler(){
		
	}
	
	public static void preInit(){
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}
	
	// Rock
	public static SoundEvent breakRock;
	
	@SubscribeEvent
	public static void RegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		breakRock = getEvent("break_rock");
	}
	
	public static SoundEvent getEvent(string id){
		SoundEvent newSoundEvent = new SoundEvent(new ResourceLocation("factoryautomation", id));
		newSoundEvent.setRegistryName(id);
		ForgeRegistries.SOUND_EVENTS.register(newSoundEvent);
		return sound;
	}
}
