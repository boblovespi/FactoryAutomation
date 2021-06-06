package boblovespi.factoryautomation.common.block.powercable;

import boblovespi.factoryautomation.api.energy.electricity.*;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.Pair;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/20/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class Cable extends FABaseBlock
{
	protected static final VoxelShape[] CABLE_VOXEL = new VoxelShape[16];
	private static final double u = 1 / 16D;
	protected static final AxisAlignedBB[] CABLE_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 4 * u, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 4 * u, 1.0D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 4 * u, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 4 * u, 1.0D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 4 * u, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 4 * u, 1.0D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 4 * u, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 4 * u, 0.8125D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 4 * u, 1.0D) };
	private static final EnumProperty<AttachPos> WEST = EnumProperty.create("west", AttachPos.class);
	private static final EnumProperty<AttachPos> EAST = EnumProperty.create("east", AttachPos.class);
	private static final EnumProperty<AttachPos> NORTH = EnumProperty.create("north", AttachPos.class);
	private static final EnumProperty<AttachPos> SOUTH = EnumProperty.create("south", AttachPos.class);

	public Cable()
	{
		super("lv_cable", false,
				Properties.generate(Material.METAL).strength(0.2f).sound(SoundType.CLOTH),
				new Item.Properties().tab(FAItemGroups.electrical));
		setDefaultState(stateObject.getBaseState()
				.set(WEST, AttachPos.NONE)
				.set(EAST, AttachPos.NONE)
			    .set(NORTH, AttachPos.NONE)
				.set(SOUTH, AttachPos.NONE));
		for (int i = 0; i < 16; i++)
		{
			CABLE_VOXEL[i] = VoxelShapes.create(CABLE_AABB[i]);
		}
	}

	public static boolean canConnectTo(BlockState state, Direction side, IBlockReader world, BlockPos pos)
	{
		Block block = state.getBlock();
		if (FABlocks.cable.toBlock() == block)
			return true;
		if (block instanceof IEnergyBlock)
			return ((IEnergyBlock) block).canConnectCable(state, side, world, pos);
		return false;

	}

	private static int getAABBIndex(BlockState state)
	{
		int i = 0;
		boolean flag = state.get(NORTH) != AttachPos.NONE;
		boolean flag1 = state.get(EAST) != AttachPos.NONE;
		boolean flag2 = state.get(SOUTH) != AttachPos.NONE;
		boolean flag3 = state.get(WEST) != AttachPos.NONE;

		// Todo: These if statements can be optimized, see IntelliJ IDEA suggestions.
		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << Direction.NORTH.getDataIndex();
		}

		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << Direction.EAST.getDataIndex();
		}

		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << Direction.SOUTH.getDataIndex();
		}

		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << Direction.WEST.getDataIndex();
		}

		return i;
	}

	public BlockState getActualState(BlockState state, IBlockReader levelIn, BlockPos pos)
	{
		state = state.set(WEST, getAttachPosition(levelIn, pos, Direction.WEST));
		state = state.set(EAST, getAttachPosition(levelIn, pos, Direction.EAST));
		state = state.set(NORTH, getAttachPosition(levelIn, pos, Direction.NORTH));
		state = state.set(SOUTH, getAttachPosition(levelIn, pos, Direction.SOUTH));
		return state;
	}

	private AttachPos getAttachPosition(IBlockReader world, BlockPos pos, Direction facing)
	{
		BlockPos offset = pos.relative(facing);
		BlockState blockState = world.getBlockState(offset);

		if (!canConnectTo(world.getBlockState(offset), facing, world, offset) && (blockState.isNormalCube(world, offset)
				|| !canConnectUpwardsTo(world, offset.down())))
		{
			BlockState stateUp = world.getBlockState(pos.up());

			if (!stateUp.isOpaqueCube(world, offset.up()))
			{
				boolean flag = Block.hasSolidSide(world.getBlockState(offset), world, offset, Direction.UP)
						|| world.getBlockState(offset).getBlock() == Blocks.GLOWSTONE;

				if (flag && canConnectUpwardsTo(world, offset.up()))
				{
					if (blockState.isNormalCube(world, offset))
					{
						return AttachPos.UP;
					}

					return AttachPos.SIDE;
				}
			}

			return AttachPos.NONE;
		} else
		{
			return AttachPos.SIDE;
		}
	}

	private boolean canConnectUpwardsTo(IBlockReader levelIn, BlockPos pos) {
		return canConnectTo(levelIn.getBlockState(pos), null, levelIn, pos);
	}
	
	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
	{
		return Block.hasSolidSide(world.getBlockState(pos.down()), world, pos.down(), Direction.UP)
				|| world.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(WEST, EAST, NORTH, SOUTH);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context)
	{
		return CABLE_VOXEL[getAABBIndex(state)];
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onBlockAdded(BlockState state, World levelIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		// NotifyNeighborCableOfStateChange(levelIn, pos);

		BlockState actualState = getActualState(state, levelIn, pos);

		System.out.println("cable placed, checking the power connections!");

		int stop = 20;

		List<Pair<IUsesEnergy_, Integer>> machines = GetEnergyMachines(
				levelIn, pos, actualState, stop, new ArrayList<>(100));

		machines.forEach(n -> System.out.println(n.getKey().GetTe().getTileData().toString()));

		if (machines.size() <= 1)
			return;

		for (Pair<IUsesEnergy_, Integer> machine : machines)
		{
			if (machine.getKey() instanceof IProducesEnergy_)
			{
				for (Pair<IUsesEnergy_, Integer> consumer : machines)
				{
					if (consumer.getKey() instanceof IRequiresEnergy_)
					{
						System.out.println("consumer = " + consumer.getKey().GetTe().getTileData().toString());
						System.out.println("producer = " + machine.getKey().GetTe().getTileData().toString());
						EnergyNetwork_.GetFromWorld((ServerWorld) levelIn).addConnection(
								new EnergyConnection_((IProducesEnergy_) machine.getKey(),
										(IRequiresEnergy_) consumer.getKey(),
										(stop - machine.getValue()) + stop - consumer.getValue(), 0.99f, 100));
					}
				}
			}
		}

	}

	private List<Pair<IUsesEnergy_, Integer>> GetEnergyMachines(World world, BlockPos pos, BlockState state, int stop,
			List<BlockPos> prevCableLocs)
	{
		System.out.println("world = [" + world + "], pos = [" + pos + "], state = [" + state + "], stop = [" + stop
				+ "], prevCableLocs = [" + prevCableLocs + "]");

		if (stop <= 0)
			return new ArrayList<>(0);
		--stop;

		List<Pair<IUsesEnergy_, Integer>> users = new ArrayList<>(10);

		prevCableLocs.add(pos);

		AttachPos north = state.get(NORTH);
		AttachPos south = state.get(SOUTH);
		AttachPos east = state.get(EAST);
		AttachPos west = state.get(WEST);

		if (north == AttachPos.UP)
		{
			BlockPos bp = pos.north().above();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}

			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}

		} else if (north == AttachPos.SIDE)
		{
			BlockPos bp = pos.north();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}
		}

		if (west == AttachPos.UP)
		{
			BlockPos bp = pos.west().above();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}

		} else if (west == AttachPos.SIDE)
		{
			BlockPos bp = pos.west();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}
		}

		if (south == AttachPos.UP)
		{
			BlockPos bp = pos.south().above();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}

		} else if (south == AttachPos.SIDE)
		{
			BlockPos bp = pos.south();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}
		}

		if (east == AttachPos.UP)
		{
			BlockPos bp = pos.east().above();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}

		} else if (east == AttachPos.SIDE)
		{
			BlockPos bp = pos.east();
			if (!prevCableLocs.contains(bp) && world.getBlockState(bp).getBlock() == FABlocks.cable)
			{

				users.addAll(GetEnergyMachines(world, bp, world.getBlockState(bp), stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy_)
			{
				users.add(new Pair<>((IUsesEnergy_) world.getTileEntity(bp), stop));
			}
		}

		return users;
	}

	public enum AttachPos implements IStringSerializable
	{
		UP("up"), SIDE("side"), NONE("none");

		private final String name;

		AttachPos(String name)
		{
			this.name = name;
		}

		@Override
		public String getString()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
