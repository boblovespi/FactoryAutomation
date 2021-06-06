package boblovespi.factoryautomation.api.energy.electricity;

/**
 * Created by Willi on 7/9/2018.
 */
@SuppressWarnings("UnusedReturnValue")
public interface IConsumesEnergy extends IUsesEnergy
{
	double voltageRequired();

	double amperageRequired();

	double setVoltageRecieved(double voltage);

	double setAmperageRecieved(double amperage);

	double resistance();

	double powerRequired();
}
