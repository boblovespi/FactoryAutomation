package boblovespi.factoryautomation.common.tileentity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Function;

public class PipeNetwork
{
	public static final Adjacencies NONE = new Adjacencies(false, false, false, false, false, false);
	public final NodeNetwork nodeNetwork;
	public String name;
	private BlockPos dataSaver;
	private HashMap<BlockPos, Adjacencies> nodes;
	private int counter = 0;

	public PipeNetwork(BlockPos pos)
	{
		name = pos.toString();
		dataSaver = pos;
		nodes = new HashMap<>();
		nodeNetwork = new NodeNetwork(10, 20, -64, 256);
	}

	public static PipeNetwork FromNBT(BlockPos pos, CompoundTag tag)
	{
		var net = new PipeNetwork(pos);
		for (String key : tag.getAllKeys())
		{
			try
			{
				long keyVal = Long.parseLong(key);
				net.nodes.put(BlockPos.of(keyVal), Adjacencies.FromInt(tag.getInt(key)));

			} catch (Exception ignored)
			{

			}
		}
		return net;
	}

	public void Join(PipeNetwork net, Level world)
	{
		ArrayList<TEPipe> pipesToAdd = new ArrayList<>();
		for (var pos : net.nodes.keySet())
		{
			if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TEPipe te)
				pipesToAdd.add(te);
		}
		for (var pipe : pipesToAdd)
			pipe.SetPipeNetwork(this, net.nodes.get(pipe.getBlockPos()));
	}

	public void AddNode(BlockPos pos, Adjacencies adjacencies)
	{
		for (var dir : adjacencies.Dirs())
		{
			var relative = pos.relative(dir);
			if (nodes.containsKey(relative))
				nodes.put(relative, nodes.get(relative).With(dir.getOpposite(), true));
		}
		nodes.put(pos, adjacencies);
	}

	public void LeaveNode(BlockPos pos)
	{
		nodes.remove(pos);
	}

	public void Split(BlockPos pos, Level world)
	{
		ArrayList<BlockPos> toCheck = new ArrayList<>();
		for (var dir : nodes.get(pos).Dirs())
		{
			var relative = pos.relative(dir);
			toCheck.add(relative);
			nodes.put(relative, nodes.get(relative).With(dir.getOpposite(), false));
		}
		if (toCheck.isEmpty())
			return;
		var main = toCheck.get(0);
		for (var check : toCheck)
		{
			if (!IsConnected(main, check) && nodes.containsKey(check))
			{
				// construct new network
				System.out.println("make new network! yay! " + check);
				var newNet = new PipeNetwork(check);
				Walk(check, newNet, world);
			}
		}
		nodes.remove(pos);
	}

	private boolean IsConnected(BlockPos main, BlockPos check)
	{
		if (main.equals(check))
			return true;
		return AStar(main, check);
	}

	private boolean AStar(BlockPos start, BlockPos end)
	{
		HashMap<BlockPos, Integer> currentScore = new HashMap<>();
		HashMap<BlockPos, Integer> totalGuess = new HashMap<>();
		PriorityQueue<BlockPos> queue = new PriorityQueue<>(Comparator.comparingInt(totalGuess::get));
		Function<BlockPos, Integer> guesser = pos -> pos.distManhattan(end);
		currentScore.put(start, 0);
		totalGuess.put(start, guesser.apply(start));
		queue.add(start);

		while (!queue.isEmpty())
		{
			var current = queue.poll();
			if (current.equals(end))
				return true;
			for (var dir : nodes.get(current).Dirs())
			{
				var neighbor = current.relative(dir);
				var newScore = currentScore.get(current) + 1;
				if (!currentScore.containsKey(neighbor) || currentScore.get(neighbor) > newScore)
				{
					currentScore.put(neighbor, newScore);
					totalGuess.put(neighbor, newScore + guesser.apply(neighbor));
					if (!queue.contains(neighbor))
						queue.add(neighbor);
				}
			}
		}
		return false;
	}

	private void Walk(BlockPos start, PipeNetwork newNet, Level world)
	{
		Stack<BlockPos> toAdd = new Stack<>();
		HashMap<BlockPos, Adjacencies> willAdd = new HashMap<>();
		toAdd.add(start);
		BlockPos current;
		while (!toAdd.empty())
		{
			current = toAdd.pop();
			willAdd.put(current, nodes.get(current));
			for (Direction dir : nodes.get(current).Dirs())
			{
				if (!willAdd.containsKey(current.relative(dir)))
					toAdd.push(current.relative(dir));
			}
		}
		for (Map.Entry<BlockPos, Adjacencies> entry : willAdd.entrySet())
		{
			var te = world.getBlockEntity(entry.getKey());
			if (te instanceof TEPipe pipe)
				pipe.SetPipeNetwork(newNet, entry.getValue());
		}
	}

	public BlockPos GetDataSaver()
	{
		return dataSaver;
	}

	public CompoundTag ToNBT()
	{
		var tag = new CompoundTag();
		tag.putLong("dataSaver", dataSaver.asLong());
		for (Map.Entry<BlockPos, Adjacencies> entry : nodes.entrySet())
		{
			tag.putInt(String.valueOf(entry.getKey().asLong()), entry.getValue().AsInt());
		}
		return tag;
	}

	public void Load(Level world)
	{
		for (BlockPos pos : nodes.keySet())
		{
			if (world.getBlockEntity(pos) instanceof TEPipe pipe)
				pipe.SetPipeNetworkQuickly(this);
		}
	}

	public void Tick()
	{
		if (nodeNetwork != null)
		{
			nodeNetwork.Tick();
			if (counter == 0)
				nodeNetwork.Cycle();
			counter += 1;
			counter %= nodeNetwork.ticksPerCycle;
		}
	}

	public record Adjacencies(boolean north, boolean south, boolean east, boolean west, boolean up, boolean down)
	{
		public static Adjacencies FromInt(int vector)
		{
			var adj = NONE;
			if ((vector & 0b000001) != 0)
				adj = adj.WithNorth(true);
			if ((vector & 0b000010) != 0)
				adj = adj.WithSouth(true);
			if ((vector & 0b000100) != 0)
				adj = adj.WithEast(true);
			if ((vector & 0b001000) != 0)
				adj = adj.WithWest(true);
			if ((vector & 0b010000) != 0)
				adj = adj.WithUp(true);
			if ((vector & 0b100000) != 0)
				adj = adj.WithDown(true);
			return adj;
		}

		public Adjacencies Clone()
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithNorth(boolean north)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithSouth(boolean south)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithEast(boolean east)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithWest(boolean west)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithUp(boolean up)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies WithDown(boolean down)
		{
			return new Adjacencies(north, south, east, west, up, down);
		}

		public Adjacencies With(Direction dir, boolean val)
		{
			return switch (dir)
						   {
							   case DOWN -> WithDown(val);
							   case UP -> WithUp(val);
							   case NORTH -> WithNorth(val);
							   case SOUTH -> WithSouth(val);
							   case WEST -> WithWest(val);
							   case EAST -> WithEast(val);
						   };
		}

		public Direction[] Dirs()
		{
			ArrayList<Direction> dirs = new ArrayList<>();
			if (north)
				dirs.add(Direction.NORTH);
			if (south)
				dirs.add(Direction.SOUTH);
			if (east)
				dirs.add(Direction.EAST);
			if (west)
				dirs.add(Direction.WEST);
			if (up)
				dirs.add(Direction.UP);
			if (down)
				dirs.add(Direction.DOWN);
			return dirs.toArray(Direction[]::new);
		}

		public int AsInt()
		{
			int vector = 0;
			if (north)
				vector |= 0b000001;
			if (south)
				vector |= 0b000010;
			if (east)
				vector |= 0b000100;
			if (west)
				vector |= 0b001000;
			if (up)
				vector |= 0b010000;
			if (down)
				vector |= 0b100000;
			return vector;
		}
	}
}
