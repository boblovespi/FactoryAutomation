package boblovespi.factoryautomation.api.pollution;

/**
 * Created by Willi on 5/18/2018.
 *
 * pollution capability
 */
public interface IPollutedChunk
{
	float GetPollution();

	void AddPollution(float amount);
}
