package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IStoresEnergy_ extends IRequiresEnergy_, IProducesEnergy_
{
	/**
	 * @return The amount of energy currently stored in the machine
	 */
	float GetStoredEnergy();
}
