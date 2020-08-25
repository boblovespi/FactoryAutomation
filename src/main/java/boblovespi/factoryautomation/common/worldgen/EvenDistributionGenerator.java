package boblovespi.factoryautomation.common.worldgen;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;

/**
 * Created by Willi on 7/5/2018.
 */
public class EvenDistributionGenerator extends Feature<OreFeatureConfig>
{
	private final SharedSeedRandom r;
	private final PerlinNoiseGenerator xOff;
	private final PerlinNoiseGenerator yOff;

	public EvenDistributionGenerator(long seed, int spacing, int levels, OreFeatureConfig config)
	{
		super((n) -> config);
		r = new SharedSeedRandom(seed);
		xOff = new PerlinNoiseGenerator(r, levels, spacing);
		yOff = new PerlinNoiseGenerator(r, levels, spacing);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator generator, Random rand, BlockPos pos, OreFeatureConfig config)
	{
		return false;
	}
}
