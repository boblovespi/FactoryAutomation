package boblovespi.factoryautomation.api.energy.heat;

import boblovespi.factoryautomation.api.energy.EnergyConstants;

/**
 * Created by Willi on 10/27/2018.
 */
public interface IHeatUser
{
	/**
	 * @return the temperature in Celsius
	 */
	float GetTemperature();

	/**
	 * @return the heat capacity of the thing in Joules/Kelvin
	 */
	float GetHeatCapacity();

	default float GetEnergy()
	{
		return GetHeatCapacity() * (GetTemperature() + 273) * EnergyConstants.GAS_CONSTANT;
	}

	void TransferEnergy(float energyAmount);

	float GetConductivity();
}
