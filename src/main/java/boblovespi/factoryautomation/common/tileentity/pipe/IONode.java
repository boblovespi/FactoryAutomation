package boblovespi.factoryautomation.common.tileentity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class IONode
{
	private final int maxBuffer;
	private NodeNetwork network;
	private BlockPos pos;
	private Direction facing;
	private int inBuffer;
	private int outBuffer;
	private boolean isInput;

	public IONode(NodeNetwork network, BlockPos pos, Direction facing)
	{
		this.network = network;
		this.pos = pos;
		this.facing = facing;
		this.maxBuffer = network.ioRate * network.ticksPerCycle;
		isInput = false;
		network.AddNode(this);
	}

	public boolean IsInput()
	{
		return isInput;
	}

	public boolean IsOutput()
	{
		return !isInput;
	}

	public void SetInput(boolean isInput)
	{
		this.isInput = isInput;
	}

	public void PushToNetwork()
	{
		var accepted = network.AddToYBuffer(pos.getY(), inBuffer);
		inBuffer -= accepted;
	}

	int OutputBufferSpace()
	{
		return maxBuffer - outBuffer;
	}

	int OutputBuffer()
	{
		return outBuffer;
	}

	int AddToOutBuffer(int amt)
	{
		var actual = Math.min(amt, OutputBufferSpace());
		outBuffer += actual;
		return actual;
	}

	void DrainOutput(int amt)
	{
		outBuffer = Mth.clamp(outBuffer - amt, 0, maxBuffer);
	}

	public int GetY()
	{
		return pos.getY();
	}

	public Direction Facing()
	{
		return facing;
	}

	public int AddToInBuffer(int amt)
	{
		var actual = Math.min(amt, maxBuffer - inBuffer);
		inBuffer += actual;
		return actual;
	}

	public boolean IsEmpty()
	{
		return inBuffer == 0 && outBuffer == 0;
	}
}
