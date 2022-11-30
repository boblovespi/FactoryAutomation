package boblovespi.factoryautomation.api.misc;

/**
 * Created by Willi on 4/27/2019.
 */
public interface IBellowsable
{
	/**
	 * @param efficiency the efficiency of the bellows application, from 50% to 100%
	 * @param time       the time in ticks to blow
	 */
	void Blow(float efficiency, int time);
}
