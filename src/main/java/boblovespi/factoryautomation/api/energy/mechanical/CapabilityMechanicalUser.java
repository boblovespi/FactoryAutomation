package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Created by Willi on 5/18/2018.
 * a class that does something
 */
public class CapabilityMechanicalUser
{
	public static Capability<IMechanicalUser> MECHANICAL_USER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
