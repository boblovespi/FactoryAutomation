package boblovespi.factoryautomation.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Willi on 7/5/2018.
 */
public class EvenDistributionGenerator extends Feature<OreConfiguration>
{
	private final WorldgenRandom r;
	private final PerlinSimplexNoise xOff;
	private final PerlinSimplexNoise yOff;

	public EvenDistributionGenerator(long seed, int spacing, int levels, Codec<OreConfiguration> codec)
	{
		super(codec);
		this.r = new WorldgenRandom(seed);
		this.xOff = new PerlinSimplexNoise(r, Arrays.asList(levels, spacing));
		this.yOff = new PerlinSimplexNoise(r, Arrays.asList(levels, spacing));
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator generator, Random rand, BlockPos pos, OreConfiguration config)
	{
		return false;
	}
}
