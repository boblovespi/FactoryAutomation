package boblovespi.factoryautomation.api.pollution;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Created by Willi on 5/18/2018.
 * a class that does something
 */
public class CapabilityPollutedChunk
{
	public static Capability<IPollutedChunk> POLLUTED_CHUNK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
