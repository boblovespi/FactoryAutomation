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

	float GetSubstanceAmount();

	default float GetEnergy()
	{
		return GetSubstanceAmount() * (GetTemperature() + 273) * EnergyConstants.GAS_CONSTANT;
	}

	void TransferEnergy(float energyAmount);

	float GetConductivity();
}
