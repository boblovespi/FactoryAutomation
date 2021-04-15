package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Ore;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Willi on 4/4/2018.
 */
public class SwampFloorOreGenerator extends Feature<NoFeatureConfig>
{
	private final float spawnChance;
	private final float lowCutoff;
	private final float midCutoff;
	private Ore ore;
	private int radius;

	public SwampFloorOreGenerator(Ore ore, int radius, float lowCutoff, float midCutoff, float spawnChance,
			Function<Dynamic<?>, ? extends NoFeatureConfig> config)
	{
		super(config);
		this.ore = ore;
		this.radius = radius;
		this.lowCutoff = lowCutoff;
		this.midCutoff = midCutoff;
		this.spawnChance = spawnChance;
	}

	@Override
	public boolean place(IWorld levelIn, ChunkGenerator generator, Random rand, BlockPos basePos,
			NoFeatureConfig config)
	{
		if (worldIn.getBiome(basePos).getCategory() != Biome.Category.SWAMP)
			return false;

		Ore.Grade type;

		float r = rand.nextFloat(); // decides grade of ore
		if (r < lowCutoff)
			type = Ore.Grade.POOR;
		else if (r < midCutoff)
			type = Ore.Grade.NORMAL;
		else
			type = Ore.Grade.RICH;

		BlockState toGen = ore.GetBlock(type).getDefaultState();

		for (int y = -10; y < 10; y++)
		{

			for (int x = -radius; x < radius; x++)
			{
				for (int z = -radius; z < radius; z++)
				{
					BlockPos pos = basePos.add(x, y, z);

					//					System.out.println(
					//							"worldIn = [" + levelIn + "], rand = [" + rand
					//									+ "], basePos = [" + basePos + "]");
					//					System.out.println("pos = " + pos);

					if (pos.distanceSq(basePos) <= radius * radius && levelIn.getFluidState(pos.up()).getFluid()
																			 .isEquivalentTo(Fluids.WATER) && levelIn
							.getFluidState(pos).isEmpty())
					{
						if (rand.nextFloat() < spawnChance * (1 - pos.down(y).distanceSq(basePos) / (radius * radius)))
							setBlockState(worldIn, pos, toGen);
					}
				}
			}
		}

		return true;
	}
}
