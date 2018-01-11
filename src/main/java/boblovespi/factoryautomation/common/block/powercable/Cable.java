package boblovespi.factoryautomation.common.block.powercable;

import boblovespi.factoryautomation.api.energy.*;
import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
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
	private static final PropertyEnum<AttachPos> WEST = PropertyEnum
			.create("west", AttachPos.class);
	private static final PropertyEnum<AttachPos> EAST = PropertyEnum
			.create("east", AttachPos.class);
	private static final PropertyEnum<AttachPos> NORTH = PropertyEnum
			.create("north", AttachPos.class);
	private static final PropertyEnum<AttachPos> SOUTH = PropertyEnum
			.create("south", AttachPos.class);

	public Cable()
	{
		super(Material.CIRCUITS);
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		FABlocks.blocks.add(this);
		FAItems.items.add(new ItemBlock(this)
								  .setRegistryName(this.getRegistryName()));
		setDefaultState(
				blockState.getBaseState().withProperty(WEST, AttachPos.NONE)
						  .withProperty(EAST, AttachPos.NONE)
						  .withProperty(NORTH, AttachPos.NONE)
						  .withProperty(SOUTH, AttachPos.NONE));
	}

	public static boolean CanConnectTo(IBlockState state, EnumFacing side,
			IBlockAccess world, BlockPos pos)
	{
		Block block = state.getBlock();
		if (Block.isEqualTo(FABlocks.cable.ToBlock(), block))
			return true;
		if (block instanceof IEnergyBlock)
			return ((IEnergyBlock) block)
					.CanConnectCable(state, side, world, pos);
		return false;

	}

	private static int getAABBIndex(IBlockState state)
	{
		int i = 0;
		boolean flag = state.getValue(NORTH) != AttachPos.NONE;
		boolean flag1 = state.getValue(EAST) != AttachPos.NONE;
		boolean flag2 = state.getValue(SOUTH) != AttachPos.NONE;
		boolean flag3 = state.getValue(WEST) != AttachPos.NONE;

		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
		}

		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << EnumFacing.EAST.getHorizontalIndex();
		}

		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
		}

		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << EnumFacing.WEST.getHorizontalIndex();
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
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn,
			BlockPos pos)
	{
		state = state.withProperty(WEST, this.GetAttachPosition(worldIn, pos,
																EnumFacing.WEST));
		state = state.withProperty(EAST, this.GetAttachPosition(worldIn, pos,
																EnumFacing.EAST));
		state = state.withProperty(NORTH, this.GetAttachPosition(worldIn, pos,
																 EnumFacing.NORTH));
		state = state.withProperty(SOUTH, this.GetAttachPosition(worldIn, pos,
																 EnumFacing.SOUTH));
		return state;
	}

	private AttachPos GetAttachPosition(IBlockAccess worldIn, BlockPos pos1,
			EnumFacing facing)
	{
		BlockPos blockpos = pos1.offset(facing);
		IBlockState iblockstate = worldIn.getBlockState(pos1.offset(facing));

		if (!CanConnectTo(
				worldIn.getBlockState(blockpos), facing, worldIn, blockpos) && (
				iblockstate.isNormalCube() || !CanConnectUpwardsTo(
						worldIn, blockpos.down())))
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos1.up());

			if (!iblockstate1.isNormalCube())
			{
				boolean flag = worldIn.getBlockState(blockpos)
									  .isSideSolid(worldIn, blockpos,
												   EnumFacing.UP)
						|| worldIn.getBlockState(blockpos).getBlock()
						== Blocks.GLOWSTONE;

				if (flag && CanConnectUpwardsTo(worldIn, blockpos.up()))
				{
					if (iblockstate.isBlockNormalCube())
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

			for (EnumFacing enumfacing : EnumFacing.values())
			{
				world.notifyNeighborsOfStateChange(
						pos.offset(enumfacing), this, false);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down())
					  .isSideSolid(worldIn, pos, EnumFacing.UP)
				|| worldIn.getBlockState(pos.down()).getBlock()
				== Blocks.GLOWSTONE;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, WEST, EAST, NORTH, SOUTH);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source,
			BlockPos pos)
	{
		return CABLE_AABB[getAABBIndex(state.getActualState(source, pos))];
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		// NotifyNeighborCableOfStateChange(worldIn, pos);

		int stop = 20;

		List<Pair<IUsesEnergy, Integer>> machines = GetEnergyMachines(
				worldIn, pos, state, stop, new ArrayList<>(100));

		if (machines.size() <= 1)
			return;

		for (Pair<IUsesEnergy, Integer> machine : machines)
		{
			if (machine.getKey() instanceof IProducesEnergy)
			{
				for (Pair<IUsesEnergy, Integer> consumer : machines)
				{
					if (consumer.getKey() instanceof IRequiresEnergy)
					{
						EnergyNetwork.GetFromWorld(worldIn).AddConnection(
								new EnergyConnection(
										(IProducesEnergy) machine.getKey(),
										(IRequiresEnergy) consumer.getKey(),
										(stop - machine.getValue()) + stop
												- consumer.getValue(), 0.99f,
										100));
					}
				}
			}
		}

	}

	private List<Pair<IUsesEnergy, Integer>> GetEnergyMachines(World world,
			BlockPos pos, IBlockState state, int stop,
			List<BlockPos> prevCableLocs)
	{
		if (stop <= 0)
			return new ArrayList<>(0);
		--stop;

		List<Pair<IUsesEnergy, Integer>> users = new ArrayList<>(10);

		prevCableLocs.add(pos);

		AttachPos north = state.getValue(NORTH);
		AttachPos south = state.getValue(SOUTH);
		AttachPos east = state.getValue(EAST);
		AttachPos west = state.getValue(WEST);

		if (north == AttachPos.UP)
		{
			BlockPos bp = pos.north().up();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}

			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}

		} else if (north == AttachPos.SIDE)
		{
			BlockPos bp = pos.north();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}
		}

		if (west == AttachPos.UP)
		{
			BlockPos bp = pos.west().up();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}

		} else if (west == AttachPos.SIDE)
		{
			BlockPos bp = pos.west();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}
		}

		if (south == AttachPos.UP)
		{
			BlockPos bp = pos.south().up();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}

		} else if (south == AttachPos.SIDE)
		{
			BlockPos bp = pos.south();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}
		}

		if (east == AttachPos.UP)
		{
			BlockPos bp = pos.east().up();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
			}

		} else if (east == AttachPos.SIDE)
		{
			BlockPos bp = pos.east();
			if (!prevCableLocs.contains(bp))
			{

				users.addAll(
						GetEnergyMachines(world, bp, world.getBlockState(bp),
										  stop, prevCableLocs));
			}
			if (world.getTileEntity(bp) instanceof IUsesEnergy)
			{
				users.add(new Pair<>((IUsesEnergy) world.getTileEntity(bp),
									 stop));
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
