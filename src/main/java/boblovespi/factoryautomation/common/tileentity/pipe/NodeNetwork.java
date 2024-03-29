package boblovespi.factoryautomation.common.tileentity.pipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;

public class NodeNetwork
{
	public final int ioRate;
	public final int ticksPerCycle;
	private int startY;
	private int endY;
	private int maxBuffer; // ioRate * ticksPerCycle * # nodes
	private int[] yBuffer;
	private ArrayList<IONode>[] nodes;
	private int nodeCount;
	Fluid fluid;

	public NodeNetwork(int ioRate, int ticksPerCycle, int startY, int endY)
	{
		this.ioRate = ioRate;
		this.ticksPerCycle = ticksPerCycle;
		this.startY = startY;
		this.endY = endY;
		yBuffer = new int[endY - startY + 1];
		nodes = new ArrayList[endY - startY + 1];
		for (int i = 0; i < nodes.length; i++)
		{
			nodes[i] = new ArrayList<>();
		}
		nodeCount = 0;
		fluid = Fluids.EMPTY;
	}

	private float GetBuffer(int y)
	{
		return yBuffer[YToIndexVal(y)];
	}

	public void Cycle()
	{
		// System.out.println(Arrays.toString(yBuffer));
		for (ArrayList<IONode> nodeList : nodes)
			nodeList.forEach(IONode::PushToNetwork);

		var y = 0;
		var yBufferEmpty = false;
		for (int h = 0; h < yBuffer.length; h++)
		{
			var isFull = nodes[h].isEmpty();
			while (!isFull)
			{
				y = Math.max(h, y);
				var left = nodes[h].stream().filter(IONode::IsOutput).mapToInt(IONode::OutputBufferSpace).max().orElse(0);
				int count = (int) nodes[h].stream().filter(IONode::IsOutput).count();
				if (count == 0)
					break;
				var toGive = Math.min(yBuffer[y] / count, left);
				for (var node : nodes[h])
				{
					if (node.IsOutput())
					{
						var given = node.AddToOutBuffer(toGive);
						yBuffer[y] -= given;
					}
				}
				if (nodes[h].stream().filter(IONode::IsOutput).mapToInt(IONode::OutputBufferSpace).max().orElse(0) > 0)
					y++;
				else isFull = true;
				if (y >= yBuffer.length) yBufferEmpty = true;
				if (yBufferEmpty) break;
			}
			if (yBufferEmpty) break;
		}
	}

	public void Tick()
	{
		for (int buffer : yBuffer)
		{
			if (buffer != 0)
				return;
		}
		for (var nodeList : nodes)
		{
			for (IONode node : nodeList)
			{
				if (!node.IsEmpty())
					return;
			}
		}
		fluid = Fluids.EMPTY;
	}

	public int AddToYBuffer(int y, int amt)
	{
		y = YToIndexVal(y);
		var accepted = Mth.clamp(amt, 0, maxBuffer - yBuffer[y]);
		yBuffer[y] += accepted;
		return accepted;
	}

	public void AddNode(IONode node)
	{
		nodes[YToIndexVal(node.GetY())].add(node);
		nodeCount++;
		maxBuffer += ioRate * ticksPerCycle;
	}

	public int YToIndexVal(int y)
	{
		return Mth.clamp(y - startY, 0, yBuffer.length - 1);
	}

	public Tag ToNBT()
	{
		var tag = new CompoundTag();
		tag.putIntArray("yBuffer", yBuffer);
		return tag;
	}

	public void LoadNBT(CompoundTag tag)
	{
		yBuffer = tag.getIntArray("yBuffer");
	}

	public void RemoveNode(IONode node)
	{
		var remove = nodes[YToIndexVal(node.GetY())].remove(node);
		if (remove)
		{
			nodeCount--;
			maxBuffer -= ioRate * ticksPerCycle;
		}
	}

	public void Join(NodeNetwork other)
	{
		for (int i = 0; i < yBuffer.length; i++)
		{
			yBuffer[i] += other.yBuffer[i];
		}
	}
}
