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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

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
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
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
		return 6000;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean activated = state.getValue(ACTIVATED);
		if (activated)
		{
			world.setBlockState(pos, FABlocks.charcoalPile.ToBlock().getDefaultState());
			for (EnumFacing dir : EnumFacing.values())
			{
				BlockPos offset = pos.offset(dir);
				IBlockState state1 = world.getBlockState(offset);
				if (state1.getBlock() == FABlocks.terraclayBrickBlock)
				{
					boolean foundPile = false;
					for (int i = 4; i >= 0; i--)
					{
						BlockPos pos1 = offset.offset(dir, i);
						IBlockState state2 = world.getBlockState(pos1);
						if (foundPile)
						{
							if (state2.getBlock() == FABlocks.terraclayBrickBlock && isSurrounded(world, pos1, null))
								world.setBlockState(pos1, Blocks.BRICK_BLOCK.getDefaultState());
						} else if (state2.getBlock() == this && state2.getValue(ACTIVATED))
							foundPile = true;
					}
				}
			}
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

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		if (!state.getValue(ACTIVATED))
			return;
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		world.spawnParticle(EnumParticleTypes.LAVA, x, y, z, rand.nextDouble() / 20d, rand.nextDouble() / 20d,
				rand.nextDouble() / 20d);
		world.spawnParticle(
				EnumParticleTypes.SMOKE_NORMAL, x, y + 1.5, z, rand.nextDouble() / 20d, 0.05, rand.nextDouble() / 20d);
		world.spawnParticle(
				EnumParticleTypes.SMOKE_NORMAL, x, y + 1.5, z, rand.nextDouble() / 20d, 0.05, rand.nextDouble() / 20d);
	}

	private boolean isSurrounded(World world, BlockPos pos, @Nullable Predicate<IBlockState> block)
	{
		for (EnumFacing dir : EnumFacing.values())
		{
			BlockPos offset = pos.offset(dir);
			IBlockState state = world.getBlockState(offset);
			if (block == null)
			{
				if (!state.isSideSolid(world, offset, dir.getOpposite()) && state.getBlock() != this)
					return false;
			} else
			{
				if (!block.test(state) && state.getBlock() != this)
					return false;
			}
		}
		return true;
	}
}
