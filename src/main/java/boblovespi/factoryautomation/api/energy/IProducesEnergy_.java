package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IProducesEnergy_ extends IUsesEnergy_
{
	/**
	 * @return The amount of energy that the machine produces, including any extracted energy
	 */
	float AmountProduced();

	/**
	 * @return The amount of energy produced minus the amount extracted
	 */
	float ActualAmountProduced();

	/**
	 * @param amount   The amount of energy to extract
	 * @param simulate Whether or not to simulate the extraction; ie. if <code>simulate == true</code>, then no energy will actually be extracted
	 * @return The amount of energy extracted (or simulated extracted)
	 */
	float ExtractEnergy(float amount, boolean simulate);
}
