package boblovespi.factoryautomation.api.energy;

/**
 * Created by Willi on 7/9/2018.
 */
public interface IConsumesEnergy extends IUsesEnergy
{
	double VoltageRequired();

	double AmperageRequired();

	double SetVoltageRecieved(double voltage);

	double SetAmperageRecieved(double amperage);

	double Resistance();

	double PowerRequired();
}
