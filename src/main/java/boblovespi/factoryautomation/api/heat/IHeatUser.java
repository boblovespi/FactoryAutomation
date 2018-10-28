package boblovespi.factoryautomation.api.heat;

/**
 * Created by Willi on 10/27/2018.
 */
public interface IHeatUser
{
	/**
	 * @return the temperature in Celsius
	 */
	float GetTemperature();

	float GetMass();

	default float GetEnergy()
	{
		return GetMass() * (GetTemperature() + 273);
	}
}
