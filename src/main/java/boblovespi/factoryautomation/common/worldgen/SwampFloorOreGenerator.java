package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Ore;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

/**
 * Created by Willi on 4/4/2018.
 */
public class SwampFloorOreGenerator extends Feature<NoneFeatureConfiguration>
{
	private final float spawnChance;
	private final float lowCutoff;
	private final float midCutoff;
	private Ore ore;
	private int radius;

	public SwampFloorOreGenerator(Ore ore, int radius, float lowCutoff, float midCutoff, float spawnChance, Codec<NoneFeatureConfiguration> config)
	{
		super(config);
		this.ore = ore;
		this.radius = radius;
		this.lowCutoff = lowCutoff;
		this.midCutoff = midCutoff;
		this.spawnChance = spawnChance;
	}

	@Override
	public boolean place(WorldGenLevel levelIn, ChunkGenerator generator, Random rand, BlockPos basePos, NoneFeatureConfiguration config)
	{
		basePos = basePos.offset(0, generator.getSeaLevel(), 0);
		if (levelIn.getBiome(basePos).getBiomeCategory() != Biome.BiomeCategory.SWAMP)
			return false;

		Ore.Grade type;

		float r = rand.nextFloat(); // decides grade of ore
		if (r < lowCutoff)
			type = Ore.Grade.POOR;
		else if (r < midCutoff)
			type = Ore.Grade.NORMAL;
		else
			type = Ore.Grade.RICH;

		BlockState toGen = ore.GetBlock(type).defaultBlockState();

		for (int y = -10; y < 10; y++)
		{

			for (int x = -radius; x < radius; x++)
			{
				for (int z = -radius; z < radius; z++)
				{
					BlockPos pos = basePos.offset(x, y, z);

					//					System.out.println(
					//							"worldIn = [" + levelIn + "], rand = [" + rand
					//									+ "], basePos = [" + basePos + "]");
					//					System.out.println("pos = " + pos);

					if (pos.distSqr(basePos) <= radius * radius && levelIn.getFluidState(pos.above()).getType().isSame(Fluids.WATER) && levelIn.getFluidState(pos).isEmpty())
					{
						if (rand.nextFloat() < spawnChance * (1 - pos.below(y).distSqr(basePos) / (radius * radius)))
							setBlock(levelIn, pos, toGen);
					}
				}
			}
		}

		return true;
	}
}
