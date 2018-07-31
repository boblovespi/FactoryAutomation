package boblovespi.factoryautomation.common.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 7/31/2018.
 */
public class Sounds
{
	public static SoundEvent guidebook = Create(new ResourceLocation(MODID, "guidebook"));

	private static SoundEvent Create(ResourceLocation name)
	{
		return new SoundEvent(name).setRegistryName(name);
	}
}
