package boblovespi.factoryautomation.common.util;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHandler {

	// Rock
	public static SoundEvent breakRock = new SoundEvent(new ResourceLocation("factoryautomation", "break_rock"));
	public static SoundType rock = new SoundType(1f, 1f, breakRock, null, null, null, null);

	public static void register() {
		SoundEvent.registerSounds();
	}
}
