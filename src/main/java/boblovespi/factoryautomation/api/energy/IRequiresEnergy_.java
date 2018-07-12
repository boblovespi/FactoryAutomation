package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IRequiresEnergy_ extends IUsesEnergy_
{
	boolean NeedsEnergy();

	/**
	 * @return The amount of energy the machine requires, not including the amount of energy already inserted
	 */
	float AmountNeeded();

	/**
	 * @return The amount of energy the machine requires minus the amount of energy already inserted
	 */
	float ActualAmountNeeded();

	/**
	 * Insert energy into a machine that needs energy
	 *
	 * @param amount   The amount of energy to insert
	 * @param simulate Whether or not to simulate the insertion; ie. if {@code simulate == true}, then no energy will actually be insertion
	 * @return Whether or not the energy was consumed by the machine
	 */
	boolean InsertEnergy(float amount, boolean simulate);
}
