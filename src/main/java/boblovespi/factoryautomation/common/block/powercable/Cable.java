package boblovespi.factoryautomation.common.block.powercable;

import boblovespi.factoryautomation.api.energy.electricity.*;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.EnumProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/20/2017.
 */
public class Cable extends Block implements FABlock
{
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
		super(Material.CIRCUITS);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		FABlocks.blocks.add(this);
		FAItems.items.add(new FAItemBlock(this));
		setDefaultState(blockState.getBaseState().withProperty(WEST, AttachPos.NONE).withProperty(EAST, AttachPos.NONE)
								  .withProperty(NORTH, AttachPos.NONE).withProperty(SOUTH, AttachPos.NONE));
	}

	public static boolean CanConnectTo(BlockState state, Direction side, IBlockAccess world, BlockPos pos)
	{
		Block block = state.getBlock();
		if (Block.isEqualTo(FABlocks.cable.ToBlock(), block))
			return true;
		if (block instanceof IEnergyBlock)
			return ((IEnergyBlock) block).CanConnectCable(state, side, world, pos);
		return false;

	}

	private static int getAABBIndex(BlockState state)
	{
		int i = 0;
		boolean flag = state.getValue(NORTH) != AttachPos.NONE;
		boolean flag1 = state.getValue(EAST) != AttachPos.NONE;
		boolean flag2 = state.getValue(SOUTH) != AttachPos.NONE;
		boolean flag3 = state.getValue(WEST) != AttachPos.NONE;

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

	@Override
	public String UnlocalizedName()
	{
		return "lv_cable";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		state = state.withProperty(WEST, this.GetAttachPosition(worldIn, pos, Direction.WEST));
		state = state.withProperty(EAST, this.GetAttachPosition(worldIn, pos, Direction.EAST));
		state = state.withProperty(NORTH, this.GetAttachPosition(worldIn, pos, Direction.NORTH));
		state = state.withProperty(SOUTH, this.GetAttachPosition(worldIn, pos, Direction.SOUTH));
		return state;
	}

	private AttachPos GetAttachPosition(IBlockAccess worldIn, BlockPos pos1, Direction facing)
	{
		BlockPos blockpos = pos1.offset(facing);
		BlockState BlockState = worldIn.getBlockState(pos1.offset(facing));

		if (!CanConnectTo(worldIn.getBlockState(blockpos), facing, worldIn, blockpos) && (BlockState.isNormalCube()
				|| !CanConnectUpwardsTo(worldIn, blockpos.down())))
		{
			BlockState iblockstate1 = worldIn.getBlockState(pos1.up());

			if (!iblockstate1.isNormalCube())
			{
				boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, Direction.UP)
						|| worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

				if (flag && CanConnectUpwardsTo(worldIn, blockpos.up()))
				{
					if (BlockState.isBlockNormalCube())
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

	private boolean CanConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
	{
		return CanConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
	}

	public void NotifyNeighborCableOfStateChange(World world, BlockPos pos)
	{
		if (Block.isEqualTo(world.getBlockState(pos).getBlock(), this))
		{
			world.notifyNeighborsOfStateChange(pos, this, false);

			for (Direction Direction : Direction.values())
			{
				world.notifyNeighborsOfStateChange(pos.offset(Direction), this, false);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, Direction.UP)
				|| worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
	}

	@Override
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, WEST, EAST, NORTH, SOUTH);
	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
	{
		return CABLE_AABB[getAABBIndex(state.getActualState(source, pos))];
	}

	@Override
	public boolean isFullCube(BlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(BlockState state)
	{
		return false;
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 *
	 * @param worldIn
	 * @param pos
	 * @param state
	 */
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
	{
		// NotifyNeighborCableOfStateChange(worldIn, pos);

		BlockState actualState = getActualState(state, worldIn, pos);

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
						EnergyNetwork_.GetFromWorld(worldIn).AddConnection(
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

		AttachPos north = state.getValue(NORTH);
		AttachPos south = state.getValue(SOUTH);
		AttachPos east = state.getValue(EAST);
		AttachPos west = state.getValue(WEST);

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
		public String getName()
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
