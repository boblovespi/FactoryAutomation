package boblovespi.breadboard.component;

/**
 * Created by Willi on 5/12/2018.
 */
public class BreadboardConnection
{
	private ILogicUnit toInput;
	private ILogicUnit toOutput;
	private int toInputPort;
	private int toOutputPort;

	public void update()
	{
		toInput.setInput(toOutput.getOutput(toOutputPort), toInputPort);
	}
}
