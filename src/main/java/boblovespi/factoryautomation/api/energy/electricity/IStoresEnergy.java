package boblovespi.factoryautomation.api.energy.electricity;

/**
 * Created by Willi on 7/9/2018.
 */
public interface IStoresEnergy extends IConsumesEnergy, IProducesEnergy
{
	/**
	 * Returns whether the battery is charging
	 */
	boolean isCharging();

	/**
	 * Returns whether the battery is discharging
	 */
	default boolean isDischarging()
	{
		return !isCharging();
	}
}
