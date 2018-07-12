package boblovespi.factoryautomation.api.energy;

import boblovespi.factoryautomation.api.energy.enums.WireType;

/**
 * Created by Willi on 7/11/2018.
 */
public class EnergyConnection
{
	private IProducesEnergy producer;
	private IConsumesEnergy consumer;

	private double maxVoltage;
	private double maxAmperage;

	private WireType wire;
	private double wireLength;

	public EnergyConnection(IProducesEnergy producer, IConsumesEnergy consumer, double maxVoltage, double maxAmperage,
			WireType wire, double wireLength)
	{
		this.producer = producer;
		this.consumer = consumer;
		this.maxVoltage = maxVoltage;
		this.maxAmperage = maxAmperage;
		this.wire = wire;
		this.wireLength = wireLength;
	}

	public void UpdateConnection(int volts, int power)
	{
		double W, v_g, i, p, R_w, v_w, c, v_m, R_m;

		W = power;
		R_w = wire.resistance * wireLength;
		R_m = consumer.Resistance();
		if (volts == -1)
		{
			i = Math.sqrt(W / (R_w + R_m));
			v_g = W / i;
			v_m = 0;
			v_w = R_m * i;
		} else
		{
			i = W / volts;
			v_g = volts;
			v_m = 0;
			v_w = R_m * i;
		}

		consumer.SetAmperageRecieved(i);
		consumer.SetVoltageRecieved(v_m);
	}
}
