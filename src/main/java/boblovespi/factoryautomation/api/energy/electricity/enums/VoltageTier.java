package boblovespi.factoryautomation.api.energy.electricity.enums;

/**
 * Created by Willi on 7/9/2018.
 */
public enum VoltageTier
{
	ELV(10), LV(120), MV(4000), HV(70000), EHV(500000);

	public final double voltage;

	VoltageTier(int voltage)
	{
		this.voltage = voltage;
	}
}
