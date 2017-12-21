package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IStoresEnergy extends IRequiresEnergy, IProducesEnergy
{
	/**
	 * @return The amount of energy currently stored in the machine
	 */
	float GetStoredEnergy();
}
