package boblovespi.factoryautomation.common.tileentity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;

public class IONode
{
	private int maxBuffer;
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

	private IONode(BlockPos pos, Direction facing)
	{
		this.pos = pos;
		this.facing = facing;
		isInput = false;
	}

	public static IONode FromNBT(BlockPos pos, Direction facing, CompoundTag tag)
	{
		var node = new IONode(pos, facing);
		node.inBuffer = tag.getInt("inBuffer");
		node.outBuffer = tag.getInt("outBuffer");
		node.isInput = tag.getBoolean("isInput");
		return node;
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

	public Tag ToNBT()
	{
		var tag = new CompoundTag();
		tag.putInt("inBuffer", inBuffer);
		tag.putInt("outBuffer", outBuffer);
		tag.putBoolean("isInput", isInput);
		return tag;
	}

	public void SetNetwork(NodeNetwork network)
	{
		if (this.network == network)
			return;
		if (this.network != null)
		{
			this.network.RemoveNode(this);
		}
		this.network = network;
		this.maxBuffer = network.ioRate * network.ticksPerCycle;
		network.AddNode(this);
	}

	public void RemoveNetwork()
	{
		if (network != null)
			network.RemoveNode(this);
		network = null;
	}
}
