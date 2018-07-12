package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 7/9/2018.
 */
public interface IProducesEnergy extends IUsesEnergy
{
	/**
	 * @return The maximum amount of voltage that the machine produces (in volts)
	 */
	double MaxVoltageProduced();

	/**
	 * @return The actual amount of voltage that the machine is producing (in volts)
	 */
	double ActualVoltageProduced();

	/**
	 * @return The amperage that the machine is producing, including any already used (in amps)
	 */
	double TotalAmperageProduced();

	/**
	 * @return The wattage that the machine produces, including any already used (in watts)
	 */
	double TotalWattageProduced();

	/**
	 * @return The wattage that the machine produces, excluding any already used (in watts)
	 */
	double ActualWattageProduced();
}
