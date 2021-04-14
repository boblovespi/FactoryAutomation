package boblovespi.factoryautomation.common.block.powercable;

import boblovespi.factoryautomation.api.energy.electricity.*;
import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import boblovespi.factoryautomation.common.util.Pair;
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
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/20/2017.
 */
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
				Properties.of(Material.METAL).strength(0.2f).sound(SoundType.CLOTH),
				new Item.Properties().tab(FAItemGroups.electrical));
		registerDefaultState(stateDefinition.getBaseState().with(WEST, AttachPos.NONE).with(EAST, AttachPos.NONE)
									  .with(NORTH, AttachPos.NONE).with(SOUTH, AttachPos.NONE));
		for (int i = 0; i < 16; i++)
		{
			CABLE_VOXEL[i] = VoxelShapes.create(CABLE_AABB[i]);
		}
	}

	public static boolean CanConnectTo(BlockState state, Direction side, IBlockReader world, BlockPos pos)
	{
		Block block = state.getBlock();
		if (FABlocks.cable.ToBlock() == block)
			return true;
		if (block instanceof IEnergyBlock)
			return ((IEnergyBlock) block).CanConnectCable(state, side, world, pos);
		return false;

	}

	private static int getAABBIndex(BlockState state)
	{
		int i = 0;
		boolean flag = state.get(NORTH) != AttachPos.NONE;
		boolean flag1 = state.get(EAST) != AttachPos.NONE;
		boolean flag2 = state.get(SOUTH) != AttachPos.NONE;
		boolean flag3 = state.get(WEST) != AttachPos.NONE;

		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << Direction.NORTH.getHorizontalIndex();
		}

		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << Direction.EAST.getHorizontalIndex();
		}

		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << Direction.SOUTH.getHorizontalIndex();
		}

		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << Direction.WEST.getHorizontalIndex();
		}

		return i;
	}

	public BlockState GetActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		state = state.with(WEST, GetAttachPosition(worldIn, pos, Direction.WEST));
		state = state.with(EAST, GetAttachPosition(worldIn, pos, Direction.EAST));
		state = state.with(NORTH, GetAttachPosition(worldIn, pos, Direction.NORTH));
		state = state.with(SOUTH, GetAttachPosition(worldIn, pos, Direction.SOUTH));
		return state;
	}

	private AttachPos GetAttachPosition(IBlockReader world, BlockPos pos, Direction facing)
	{
		BlockPos offset = pos.offset(facing);
		BlockState blockState = world.getBlockState(offset);

		if (!CanConnectTo(world.getBlockState(offset), facing, world, offset) && (blockState.isNormalCube(world, offset)
				|| !CanConnectUpwardsTo(world, offset.down())))
		{
			BlockState stateUp = world.getBlockState(pos.up());

			if (!stateUp.isNormalCube(world, offset.up()))
			{
				boolean flag = world.getBlockState(offset).isSolidSide(world, offset, Direction.UP)
						|| world.getBlockState(offset).getBlock() == Blocks.GLOWSTONE;

				if (flag && CanConnectUpwardsTo(world, offset.up()))
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

	private boolean CanConnectUpwardsTo(IBlockReader worldIn, BlockPos pos)
	{
		return CanConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
	{
		return world.getBlockState(pos.down()).isSolidSide(world, pos.down(), Direction.UP)
				|| world.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(WEST, EAST, NORTH, SOUTH);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return CABLE_VOXEL[getAABBIndex(state)];
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		// NotifyNeighborCableOfStateChange(worldIn, pos);

		BlockState actualState = GetActualState(state, worldIn, pos);

		System.out.println("cable placed, checking the power connections!");

		int stop = 20;

		List<Pair<IUsesEnergy_, Integer>> machines = GetEnergyMachines(
				worldIn, pos, actualState, stop, new ArrayList<>(100));

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
						EnergyNetwork_.GetFromWorld((ServerWorld) worldIn).AddConnection(
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
			BlockPos bp = pos.north().up();
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
			BlockPos bp = pos.west().up();
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
			BlockPos bp = pos.south().up();
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
			BlockPos bp = pos.east().up();
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

		private String name;

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
