package boblovespi.factoryautomation.common.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;
import java.util.Set;

/**
 * Created by Willi on 12/25/2018.
 */
public class SurfaceWorldGenerator extends WorldGenerator
{
	protected final BlockState block;
	protected final int tries;
	protected final Set<Block> legalBlocks;

	public SurfaceWorldGenerator(BlockState block, int tries, Set<Block> legalBlocks)
	{
		this.block = block;
		this.tries = tries;
		this.legalBlocks = legalBlocks;
	}

	// copied from dead bush generator
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (BlockState block = worldIn.getBlockState(position);
			 (block.getBlock().isAir(block, worldIn, position) || block.getBlock().isLeaves(block, worldIn, position))
					 && position.getY() > 0; block = worldIn.getBlockState(position))
		{
			position = position.down();
		}

		for (int i = 0; i < tries; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && legalBlocks.contains(worldIn.getBlockState(blockpos.down()).getBlock()))
			{
				worldIn.setBlockState(blockpos, block, 2);
			}
		}
		return true;
	}
}
