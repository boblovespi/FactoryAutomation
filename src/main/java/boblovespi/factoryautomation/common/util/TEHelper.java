package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.IHeatUser;
import boblovespi.factoryautomation.api.energy.mechanical.IMechanicalUser;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

import static boblovespi.factoryautomation.api.energy.mechanical.CapabilityMechanicalUser.MECHANICAL_USER_CAPABILITY;

/**
 * Created by Willi on 2/20/2018.
 */
public class TEHelper
{
	public static boolean IsMechanicalFace(BlockEntity te, Direction face)
	{
		if (te != null)
		{
			var mechanicalUser = te.getCapability(MECHANICAL_USER_CAPABILITY, face);
			return mechanicalUser.isPresent() && mechanicalUser.orElse(null).HasConnectionOnSide(face);
		}
		return false;
	}

	@SuppressWarnings("ConstantConditions")
	public static IMechanicalUser GetUser(BlockEntity te, Direction face)
	{
		return te.getCapability(MECHANICAL_USER_CAPABILITY, face).orElse(null);
	}

	public static float GetTorqueOnFace(BlockEntity te, Direction face)
	{
		return IsMechanicalFace(te, face) ? GetUser(te, face).GetTorqueOnFace(face) : 0;
	}

	public static float GetSpeedOnFace(BlockEntity te, Direction face)
	{
		return IsMechanicalFace(te, face) ? GetUser(te, face).GetSpeedOnFace(face) : 0;
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

	public static MenuProvider GetContainer(BlockEntity te)
	{
		if (te instanceof MenuProvider)
			return (MenuProvider) te;
		return null;
	}
}
