package boblovespi.factoryautomation.common.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Willi on 7/5/2018.
 */
public class EvenDistributionGenerator extends WorldGenerator
{
	private final Random r;
	private final NoiseGeneratorPerlin xOff;
	private final NoiseGeneratorPerlin yOff;

	public EvenDistributionGenerator(long seed,int spacing, int levels)
	{
		r = new Random(seed);
		xOff = new NoiseGeneratorPerlin(r, levels);
		yOff = new NoiseGeneratorPerlin(r, levels);
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		return false;
	}
}
