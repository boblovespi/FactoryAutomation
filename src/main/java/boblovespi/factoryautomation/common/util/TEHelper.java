package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.IHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;

import static boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;

/**
 * Created by Willi on 2/20/2018.
 */
public class TEHelper
{
	public static boolean IsMechanicalFace(TileEntity te, Direction face)
	{
		return te != null && te.getCapability(MECHANICAL_USER_CAPABILITY, face).isPresent();
	}

	public static IMechanicalUser GetUser(TileEntity te, Direction face)
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

	public static INamedContainerProvider GetContainer(TileEntity te)
	{
		if (te instanceof INamedContainerProvider)
			return (INamedContainerProvider) te;
		return null;
	}
}
