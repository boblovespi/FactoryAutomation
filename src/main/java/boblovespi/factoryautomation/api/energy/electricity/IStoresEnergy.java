package boblovespi.factoryautomation.api.energy.electricity;

/**
 * Created by Willi on 7/9/2018.
 */
public interface IStoresEnergy extends IConsumesEnergy, IProducesEnergy
{
	/**
	 * Returns whether the battery is charging
	 */
	boolean IsCharging();

	/**
	 * Returns whether the battery is discharging
	 */
	default boolean IsDischarging()
	{
		return !IsCharging();
	}
}
