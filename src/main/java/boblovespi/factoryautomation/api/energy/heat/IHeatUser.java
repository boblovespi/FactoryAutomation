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
	float getTemperature();

	float getSubstanceAmount();

	default float getEnergy()
	{
		return getSubstanceAmount() * (getTemperature() + 273) * EnergyConstants.GAS_CONSTANT;
	}

	void transferEnergy(float energyAmount);

	float getConductivity();
}
