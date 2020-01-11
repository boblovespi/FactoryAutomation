package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.resource.Ore;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Willi on 4/4/2018.
 */
public class SwampFloorOreGenerator extends WorldGenerator
{
	private final float spawnChance;
	private final float lowCutoff;
	private final float midCutoff;
	private Ore ore;
	private int radius;

	public SwampFloorOreGenerator(Ore ore, int radius, float lowCutoff, float midCutoff, float spawnChance)
	{
		super(false);
		this.ore = ore;
		this.radius = radius;
		this.lowCutoff = lowCutoff;
		this.midCutoff = midCutoff;
		this.spawnChance = spawnChance;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos basePos)
	{
		if (worldIn.getBiome(basePos) != Biomes.SWAMPLAND)
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
					//							"worldIn = [" + worldIn + "], rand = [" + rand
					//									+ "], basePos = [" + basePos + "]");
					//					System.out.println("pos = " + pos);

					if (pos.distanceSq(basePos) <= radius * radius
							&& worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER
							&& worldIn.getBlockState(pos).getBlock() != Blocks.WATER)
					{
						if (rand.nextFloat() < spawnChance * (1 - pos.down(y).distanceSq(basePos) / (radius * radius)))
							setBlockAndNotifyAdequately(worldIn, pos, toGen);
					}
				}
			}
		}

		return true;
	}
}
