package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Willi on 2/3/2019.
 */
public class IronCharcoalMix extends FABaseBlock
{
	public static final PropertyBool ACTIVATED = LogPile.ACTIVATED;

	public IronCharcoalMix()
	{
		super(Material.ROCK, "iron_charcoal_mix", FAItemGroups.metallurgy);
		setDefaultState(getDefaultState().withProperty(ACTIVATED, false));
		setHardness(3.5f);
		setHarvestLevel("shovel", 0);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "processing/" + RegistryName();
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 6000;
	}

	@Override
	public void updateTick(World world, BlockPos pos, BlockState state, Random rand)
	{
		boolean activated = state.getValue(ACTIVATED);
		if (activated)
		{
			if (isSurrounded(
					world, pos, n -> n.getBlock() == FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
							|| n.getBlock() == FABlocks.ironBloom))
				world.setBlockState(pos, FABlocks.ironBloom.ToBlock().getDefaultState());
			else
				world.setBlockState(pos, state.withProperty(ACTIVATED, false));
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		BlockState block = world.getBlockState(fromPos);
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
			for (Direction face : Direction.values())
			{
				BlockPos offset = pos.offset(face);
				BlockState state1 = world.getBlockState(offset);
				if (state1.getBlock().isAir(state1, world, offset))
				{
					world.setBlockState(offset, Blocks.FIRE.getDefaultState());
					sidesOnFire = true;
				} else if (state1.getBlock() == Blocks.FIRE)
				{
					sidesOnFire = true;
				} else if (state1.getBlock() != FABlocks.metalPlateBlock.GetBlock(Metals.COPPER)
						&& state1.getBlock() != this && state1.getBlock() != FABlocks.ironBloom)
				{
					isSurrounded = false;
				}
			}
			if (!sidesOnFire && !isSurrounded)
				world.setBlockState(pos, state.withProperty(ACTIVATED, false));
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
	public BlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(ACTIVATED, meta == 1);
	}

	@Override
	public int getMetaFromState(BlockState state)
	{
		return state.getValue(ACTIVATED) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ACTIVATED);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand)
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

	private boolean isSurrounded(World world, BlockPos pos, @Nullable Predicate<BlockState> block)
	{
		for (Direction dir : Direction.values())
		{
			BlockPos offset = pos.offset(dir);
			BlockState state = world.getBlockState(offset);
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
