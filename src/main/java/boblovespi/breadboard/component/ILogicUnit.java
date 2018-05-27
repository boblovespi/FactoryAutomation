package boblovespi.breadboard.component;

/**
 * Created by Willi on 5/12/2018.
 */
public interface ILogicUnit
{
	int InputSize();

	int OutputSize();

	void SetInput(VoltageLevel v, int input);

	VoltageLevel GetOutput(int output);
}
