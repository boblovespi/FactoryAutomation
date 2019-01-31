package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Willi on 1/27/2019.
 */
public class LogPile extends FABaseBlock
{
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	public LogPile()
	{
		super(Material.WOOD, "log_pile", FACreativeTabs.primitive);
		setDefaultState(getDefaultState().withProperty(ACTIVATED, false));
		setHardness(1.2f);
		setHarvestLevel("axe", 0);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(ACTIVATED) ? 20 : 8;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(ACTIVATED) ? 120 : 20;
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 3000;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean activated = state.getValue(ACTIVATED);
		if (activated)
		{
			world.setBlockState(pos, FABlocks.charcoalPile.ToBlock().getDefaultState());
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		IBlockState block = world.getBlockState(fromPos);
		if (!state.getValue(ACTIVATED))
		{
			if (block.getBlock() == Blocks.FIRE || (block.getBlock() == this && block.getValue(ACTIVATED)))
			{
				world.setBlockState(pos, state.withProperty(ACTIVATED, true), 7);
				world.scheduleUpdate(pos, this, tickRate(world));
			}

		} else
		{
			boolean sidesOnFire = false;
			boolean isSurrounded = true;
			for (EnumFacing face : EnumFacing.values())
			{
				BlockPos offset = pos.offset(face);
				IBlockState state1 = world.getBlockState(offset);
				if (state1.getBlock().isAir(state1, world, offset))
				{
					world.setBlockState(offset, Blocks.FIRE.getDefaultState());
					sidesOnFire = true;
				} else if (state1.getBlock() == Blocks.FIRE)
				{
					// do something later?
					sidesOnFire = true;
				} else if (!state1.isSideSolid(world, offset, face.getOpposite()) && state1.getBlock() != this)
				{
					// if (world.rand.nextFloat() < 0.2f)
					// 	world.setBlockState(pos, Blocks.FIRE.getDefaultState());
					isSurrounded = false;
				}
			}
			if (!sidesOnFire && !isSurrounded)
				world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
	}

	/**
	 * Determines if this block should set fire and deal fire damage
	 * to entities coming into contact with it.
	 */
	@Override
	public boolean isBurning(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(ACTIVATED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(ACTIVATED, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ACTIVATED) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ACTIVATED);
	}
}
