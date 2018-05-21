package boblovespi.factoryautomation.api.pollution;

import java.util.function.Consumer;

/**
 * Created by Willi on 5/18/2018.
 * default implementation
 */
public class PollutedChunk implements IPollutedChunk
{
	private float pollution = 0;
	private Consumer<IPollutedChunk> callback;

	@Override
	public float GetPollution()
	{
		return pollution;
	}

	@Override
	public void AddPollution(float amount)
	{
		pollution += amount;
		if (callback != null)
			callback.accept(this);
	}

	void SetPollution(float amount)
	{
		pollution = amount;
	}

	public void SetPollutionCallback(Consumer<IPollutedChunk> func)
	{
		callback = func;
	}
}
