package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.item.types.MetalOres.COPPER;
import static boblovespi.factoryautomation.common.item.types.MetalOres.TIN;
import static net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES;
import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;
import static net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE;
import static net.minecraft.world.gen.placement.Placement.COUNT_HEIGHTMAP_DOUBLE;

/**
 * Created by Willi on 12/28/2017.
 * world generator class
 */
public class WorldGenHandler
{
	public static final DeferredRegister<Feature<?>> deferredRegister = new DeferredRegister<>(
			ForgeRegistries.FEATURES, MODID);
	private static RegistryObject<Feature<NoFeatureConfig>> limoniteGen = deferredRegister.register(
			"limonite_gen", () -> new SwampFloorOreGenerator((Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f,
					NoFeatureConfig::deserialize));

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
		// limoniteGen = new SwampFloorOreGenerator((Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f);
		// rockGenerator = new RockGenerator(12, surfaceBlocks);
		// flintGenerator = new SurfaceWorldGenerator(FABlocks.flintRock.ToBlock().getDefaultState(), 5, surfaceBlocks);
	}

	public static void AddFeaturesToBiomes()
	{
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND
					&& biome.getCategory() != Biome.Category.NONE)
			{
				AddOre(biome,
						new OreFeatureConfig(NATURAL_STONE, FABlocks.metalOres.GetBlock(COPPER).getDefaultState(), 10),
						CountRange(9, 64));
				AddOre(biome,
						new OreFeatureConfig(NATURAL_STONE, FABlocks.metalOres.GetBlock(TIN).getDefaultState(), 4),
						CountRange(15, 64));
				AddToBiome(biome, Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(FABlocks.flintRock.ToBlock().getDefaultState()),
								new SimpleBlockPlacer()).tries(1).build(), VEGETAL_DECORATION,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1)));
				AddToBiome(biome, Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(FABlocks.rock.ToBlock().getDefaultState()), new RockBlockPlacer())
								.tries(2).build(), VEGETAL_DECORATION,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(5)));
			}
			if (biome.getCategory() == Biome.Category.THEEND)
			{
				AddOre(biome, new OreFeatureConfig(OreFeatureConfig.FillerBlockType
						.create("END_STONE", "end_stone", new BlockMatcher(Blocks.END_STONE)),
						FABlocks.siliconQuartzOre.ToBlock().getDefaultState(), 9), CountRange(2, 255));
			}
			if (biome.getCategory() == Biome.Category.SWAMP)
			{
				AddToBiome(biome, limoniteGen.get(), NoFeatureConfig.NO_FEATURE_CONFIG, UNDERGROUND_ORES,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2)));
			}
		}
	}

	private static <T extends IFeatureConfig> void AddToBiome(Biome biome, Feature<T> feature, T config,
			GenerationStage.Decoration stage, ConfiguredPlacement<?> placement)
	{
		biome.addFeature(stage, feature.withConfiguration(config).withPlacement(placement));
	}

	private static void AddOre(Biome biome, OreFeatureConfig config, ConfiguredPlacement<?> placement)
	{
		biome.addFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(config).withPlacement(placement));
	}

	private static ConfiguredPlacement<?> CountRange(int count, int max)
	{
		return Placement.COUNT_RANGE.configure(new CountRangeConfig(count, 0, 0, max));
	}

	//	@Override
	//	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
	//			IChunkProvider chunkProvider)
	//	{
	//		switch (world.provider.getDimensionType())
	//		{
	//		case OVERWORLD:
	//			RunGenerator(copperGen, world, random, chunkX, chunkZ, 9, 0, 64);
	//			RunGenerator(tinGen, world, random, chunkX, chunkZ, 15, 0, 64);
	//			if (random.nextFloat() < 0.2)
	//				WaterFeatureGenerator(limoniteGen, world, random, chunkX, chunkZ, 1);
	//			RunGenerator(rockGenerator, world, random, chunkX, chunkZ, 2, 50, 100);
	//			RunGenerator(flintGenerator, world, random, chunkX, chunkZ, 1, 50, 100);
	//			break;
	//		case Biomes.THE_END:
	//			RunGenerator(siliconQuartzGen, world, random, chunkX, chunkZ, 2, 0, 255);
	//		default:
	//			break;
	//		}
	//	}
}
