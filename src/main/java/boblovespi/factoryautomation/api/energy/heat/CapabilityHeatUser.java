package boblovespi.factoryautomation.api.energy.heat;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Created by Willi on 10/28/2018.
 */
public class CapabilityHeatUser
{
	public static final float AIR_CONDUCTIVITY = 0.024f;
	public static Capability<IHeatUser> HEAT_USER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
