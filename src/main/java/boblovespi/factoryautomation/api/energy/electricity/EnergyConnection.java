package boblovespi.factoryautomation.api.energy.electricity;

import boblovespi.factoryautomation.api.energy.electricity.enums.WireType;

/**
 * Created by Willi on 7/11/2018.
 */
public class EnergyConnection
{
	double maxVoltage;
	double maxAmperage;
	WireType wire;
	double wireLength;
	private IProducesEnergy producer;
	private IConsumesEnergy consumer;

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

	public IProducesEnergy getProducer()
	{
		return producer;
	}

	public IConsumesEnergy getConsumer()
	{
		return consumer;
	}

	public void updateConnection(double volts, double power)
	{
		double W, v_g, i, p, R_w, v_w, c, v_m, R_m;

		W = power;
		R_w = wire.resistance * wireLength;
		R_m = consumer.resistance();
		if (volts < 0)
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

		consumer.setAmperageRecieved(i);
		consumer.setVoltageRecieved(v_m);
	}

	public double simulateUpdateConnection(double volts)
	{
		double W, v_g, i, p, R_w, v_w, c, v_m, R_m;

		R_w = wire.resistance * wireLength;
		R_m = consumer.resistance();

		v_g = volts;
		v_m = 0;
		W = (v_g * v_g) / (R_w + R_m);
		i = W / v_g;
		v_w = i * R_m;

		return i;
	}
}
