package boblovespi.factoryautomation.api.energy.electricity.enums;

/**
 * Created by Willi on 7/11/2018.
 */
public enum WireType
{
	;
	public final VoltageTier tier;
	public final double maxVoltage;
	public final double resistance;

	WireType(VoltageTier tier, double maxVoltage, double resistance)
	{
		this.tier = tier;
		this.maxVoltage = maxVoltage;
		this.resistance = resistance;
	}
}
