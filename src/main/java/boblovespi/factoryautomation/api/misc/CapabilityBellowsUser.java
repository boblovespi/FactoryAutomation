package boblovespi.factoryautomation.api.misc;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Created by Willi on 4/27/2019.
 */
public class CapabilityBellowsUser
{
	public static Capability<IBellowsable> BELLOWS_USER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
