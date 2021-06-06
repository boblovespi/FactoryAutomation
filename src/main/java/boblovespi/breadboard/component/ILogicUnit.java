package boblovespi.breadboard.component;

/**
 * Created by Willi on 5/12/2018.
 */
public interface ILogicUnit
{
	int inputSize();

	int outputSize();

	void setInput(VoltageLevel v, int input);

	VoltageLevel getOutput(int output);
}
