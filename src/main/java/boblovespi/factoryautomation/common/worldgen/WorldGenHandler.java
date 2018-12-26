package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Willi on 12/28/2017.
 * world generator class
 */
public class WorldGenHandler implements IWorldGenerator
{
	private WorldGenerator copperGen;
	private WorldGenerator tinGen;
	private WorldGenerator limoniteGen;
	private WorldGenerator siliconQuartzGen;
	private WorldGenerator rockGenerator;
	private WorldGenerator flintGenerator;

	// surface blocks
	private Set<Block> surfaceBlocks = new HashSet<Block>(5)
	{{
		add(Blocks.SAND);
		add(Blocks.STONE);
		add(Blocks.DIRT);
		add(Blocks.GRASS);
		add(Blocks.GRAVEL);
	}};

	public WorldGenHandler()
	{
		copperGen = new WorldGenMinable(FABlocks.metalOres.GetBlock(MetalOres.COPPER).getDefaultState(), 10);
		tinGen = new WorldGenMinable(FABlocks.metalOres.GetBlock(MetalOres.TIN).getDefaultState(), 4);
		limoniteGen = new SwampFloorOreGenerator((Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f);
		siliconQuartzGen = new WorldGenMinable(FABlocks.siliconQuartzOre.ToBlock().getDefaultState(), 9,
				n -> n != null && n.getBlock() == Blocks.END_STONE);
		rockGenerator = new RockGenerator(12, surfaceBlocks);
		flintGenerator = new SurfaceWorldGenerator(FABlocks.flintRock.ToBlock().getDefaultState(), 5, surfaceBlocks);
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
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimensionType())
		{
		case OVERWORLD:
			RunGenerator(copperGen, world, random, chunkX, chunkZ, 9, 0, 64);
			RunGenerator(tinGen, world, random, chunkX, chunkZ, 15, 0, 64);
			if (random.nextFloat() < 0.2)
				WaterFeatureGenerator(limoniteGen, world, random, chunkX, chunkZ, 1);
			RunGenerator(rockGenerator, world, random, chunkX, chunkZ, 2, 50, 100);
			RunGenerator(flintGenerator, world, random, chunkX, chunkZ, 1, 50, 100);
			break;
		case THE_END:
			RunGenerator(siliconQuartzGen, world, random, chunkX, chunkZ, 2, 0, 255);
		default:
			break;
		}
	}

	public void RunGenerator(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ,
			int chancesToSpawn, int minHeight, int maxHeight)
	{
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight + 1;
		for (int i = 0; i < chancesToSpawn; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16) + 8;
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunkZ * 16 + rand.nextInt(16) + 8;
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}

	public void WaterFeatureGenerator(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ,
			int chancesToSpawn)
	{
		for (int i = 0; i < chancesToSpawn; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16) + 8;
			int y = 255;
			int z = chunkZ * 16 + rand.nextInt(16) + 8;
			BlockPos column = new BlockPos(x, y, z);

			while (column.getY() > 0 && world.getBlockState(column.up()).getBlock() != Blocks.WATER)
			{
				column = column.down();
			}

			if (column.getY() <= 255)
				generator.generate(world, rand, column);
		}
	}
}
