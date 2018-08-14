package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.mechanical.IMechanicalUser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import static boblovespi.factoryautomation.api.mechanical.CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;

/**
 * Created by Willi on 2/20/2018.
 */
public class TEHelper
{
	public static boolean IsMechanicalFace(TileEntity te, EnumFacing face)
	{
		return te != null && te.hasCapability(MECHANICAL_USER_CAPABILITY, face);
	}

	public static IMechanicalUser GetUser(TileEntity te, EnumFacing face)
	{
		return te.getCapability(MECHANICAL_USER_CAPABILITY, face);
	}
}
