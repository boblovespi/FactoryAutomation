package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.IHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import static boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;

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

	public static void TransferHeat(IHeatUser from, IHeatUser to)
	{
		float K_d = from.GetTemperature() - to.GetTemperature();
		float gamma = from.GetConductivity();
		float transfer = K_d * gamma * 0.05f;
		from.TransferEnergy(-transfer);
		to.TransferEnergy(transfer);
	}

	public static void DissipateHeat(IHeatUser heatUser, int numOfSides)
	{
		float K_d = heatUser.GetTemperature() - 20f;
		float gamma = CapabilityHeatUser.AIR_CONDUCTIVITY;
		float transfer = K_d * gamma * 0.05f * numOfSides;
		heatUser.TransferEnergy(-transfer);
	}
}
