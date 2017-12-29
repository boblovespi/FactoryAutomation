package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by Willi on 12/28/2017.
 */
public class WorldGenHandler implements IWorldGenerator
{
	private WorldGenerator copperGen;
	private WorldGenerator tinGen;

	public WorldGenHandler()
	{
		copperGen = new WorldGenMinable(
				FABlocks.metalOres.ToBlock()
								  .getStateFromMeta(MetalOres.COPPER.GetId()),
				10);
		tinGen = new WorldGenMinable(
				FABlocks.metalOres.ToBlock()
								  .getStateFromMeta(MetalOres.TIN.GetId()),
				4);
	}

	/**
	 * Generate some world
	 *
	 * @param random         the chunk specific {@link Random}.
	 * @param chunkX         the chunk X coordinate of this chunk.
	 * @param chunkZ         the chunk Z coordinate of this chunk.
	 * @param world          : additionalData[0] The minecraft {@link World} we're generating for.
	 * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
	 * @param chunkProvider  : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionType())
		{
		case OVERWORLD:
			RunGenerator(copperGen, world, random, chunkX, chunkZ, 9, 0, 64);
			RunGenerator(tinGen, world, random, chunkX, chunkZ, 15, 0, 64);
			break;
		default:
			break;
		}
	}

	public void RunGenerator(WorldGenerator generator, World world, Random rand,
			int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight,
			int maxHeight)
	{
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException(
					"Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; i++)
		{
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
