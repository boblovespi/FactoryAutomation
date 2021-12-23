package boblovespi.factoryautomation.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

import java.util.Arrays;

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
		this.r = new WorldgenRandom(new LegacyRandomSource(seed));
		this.xOff = new PerlinSimplexNoise(r, Arrays.asList(levels, spacing));
		this.yOff = new PerlinSimplexNoise(r, Arrays.asList(levels, spacing));
	}

	@Override
	public boolean place(FeaturePlaceContext context)
	{
		return false;
	}
}
