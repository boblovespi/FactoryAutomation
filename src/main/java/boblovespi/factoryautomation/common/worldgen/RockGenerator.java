package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Rock.Variants;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSwamp;

import java.util.Random;
import java.util.Set;

import static boblovespi.factoryautomation.common.block.resource.Rock.VARIANTS;

/**
 * Created by Willi on 12/26/2018.
 */
public class RockGenerator extends SurfaceWorldGenerator
{
	public RockGenerator(int tries, Set<Block> legalBlocks)
	{
		super(FABlocks.rock.ToBlock().getDefaultState(), tries, legalBlocks);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position)
	{
		for (BlockState block = world.getBlockState(position);
			 (block.getBlock().isAir(block, world, position) || block.getBlock().isLeaves(block, world, position))
					 && position.getY() > 0; block = world.getBlockState(position))
		{
			position = position.down();
		}

		for (int i = 0; i < tries; ++i)
		{
			BlockPos pos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));

			if (world.isAirBlock(pos) && legalBlocks.contains(world.getBlockState(pos.down()).getBlock()))
			{
				Biome biome = world.getBiome(pos);
				if (biome instanceof BiomeDesert)
				{
					world.setBlockState(pos, block.withProperty(VARIANTS, Variants.SANDSTONE), 2);
				} else if (biome instanceof BiomeSwamp)
				{
					world.setBlockState(pos, block.withProperty(VARIANTS, Variants.MOSSY_COBBLESTONE), 2);
				} else
				{
					world.setBlockState(pos, block.withProperty(VARIANTS, Variants.values()[rand.nextInt(5)]), 2);
				}

			}
		}
		return true;
	}
}
