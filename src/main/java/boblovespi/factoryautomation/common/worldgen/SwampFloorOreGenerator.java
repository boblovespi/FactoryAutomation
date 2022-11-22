package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.util.FATags;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

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

	public SwampFloorOreGenerator(Ore ore, int radius, float lowCutoff, float midCutoff, float spawnChance,
								  Codec<NoneFeatureConfiguration> config)
	{
		super(config);
		this.ore = ore;
		this.radius = radius;
		this.lowCutoff = lowCutoff;
		this.midCutoff = midCutoff;
		this.spawnChance = spawnChance;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		var basePos = context.origin();

		Ore.Grade type;
		var rand = context.random();
		var world = context.level();
		float r = rand.nextFloat(); // decides grade of ore
		if (r < lowCutoff) type = Ore.Grade.POOR;
		else if (r < midCutoff) type = Ore.Grade.NORMAL;
		else type = Ore.Grade.RICH;

		BlockState toGen = ore.GetBlock(type).defaultBlockState();

		for (int y = -10; y < 10; y++)
		{
			for (int x = -radius; x < radius; x++)
			{
				for (int z = -radius; z < radius; z++)
				{
					BlockPos pos = basePos.offset(x, y, z);
					if (pos.distSqr(basePos) <= radius * radius
						&& FATags.Contains(FluidTags.WATER, world.getFluidState(pos.above()).getType()) && world.getFluidState(pos).isEmpty()
						&& rand.nextFloat() < spawnChance * (1 - pos.below(y).distSqr(basePos) / (radius * radius)))
						setBlock(world, pos, toGen);
				}
			}
		}
		return true;
	}
}