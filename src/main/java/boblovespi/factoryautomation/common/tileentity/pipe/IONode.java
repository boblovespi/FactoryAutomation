package boblovespi.factoryautomation.common.tileentity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class IONode
{
	private NodeNetwork network;
	private BlockPos pos;
	private Direction facing;
	private final int maxBuffer;
	private int inBuffer;
	private int outBuffer;
	private boolean isInput;

	public IONode(NodeNetwork network, BlockPos pos, Direction facing)
	{
		this.network = network;
		this.pos = pos;
		this.facing = facing;
		this.maxBuffer = network.ioRate * network.ticksPerCycle;
		network.AddNode(this);
	}

	public void Tick()
	{

	}

	public boolean IsInput()
	{
		return isInput;
	}

	public boolean IsOutput()
	{
		return !isInput;
	}

	public void PushToNetwork()
	{
		network.AddToYBuffer(pos.getY(), inBuffer);
	}

	int OutputBufferSpace()
	{
		return maxBuffer - outBuffer;
	}

	int AddToOutBuffer(int amt)
	{
		var actual = Math.min(amt, OutputBufferSpace());
		outBuffer += actual;
		return actual;
	}

	public int GetY()
	{
		return pos.getY();
	}
}
