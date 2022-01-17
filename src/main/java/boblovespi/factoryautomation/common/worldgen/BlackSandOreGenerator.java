package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlackSandOreGenerator extends Feature<NoneFeatureConfiguration>
{
	private final int radius;
	private final int extraRadius;
	private final float extraBandChance;

	public BlackSandOreGenerator(int radius, int extraRadius, Codec<NoneFeatureConfiguration> config)
	{
		super(config);
		this.radius = radius;
		this.extraRadius = extraRadius + radius;
		extraBandChance = 1f / (this.extraRadius * this.extraRadius - this.radius * this.radius);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		var pos = context.origin();
		var random = context.random();
		var world = context.level();
		BlockState toGen = FABlocks.blackSand.ToBlock().defaultBlockState();

		BlockPos.MutableBlockPos genPos = pos.mutable();
		for (int y = -10; y < 10; y++)
		{
			for (int x = -extraRadius; x < extraRadius; x++)
			{
				for (int z = -extraRadius; z < extraRadius; z++)
				{
					genPos.move(x, y, z);
					if (world.isStateAtPosition(genPos, n -> n.is(BlockTags.SAND)))
					{
						if (genPos.distSqr(pos) <= radius * radius)
							setBlock(world, genPos, toGen);
						else if (genPos.distSqr(pos) <= extraRadius * extraRadius
										 && random.nextFloat() > extraBandChance * (genPos.distSqr(pos) - radius * radius))
							setBlock(world, genPos, toGen);
					}
					genPos.move(-x, -y, -z);
				}
			}
		}
		return true;
	}
}
