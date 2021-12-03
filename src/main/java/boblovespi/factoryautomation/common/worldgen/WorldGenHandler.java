package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.item.types.MetalOres.COPPER;
import static boblovespi.factoryautomation.common.item.types.MetalOres.TIN;
import static net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES;
import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;
import staticnet.minecraft.world.level.levelgen.GenerationStep.DecorationFillerBlockType.NATURAL_STONE;


import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

/**
 * Created by Willi on 12/28/2017.
 * level generator class
 */
@Mod.EventBusSubscriber(modid = MODID)
public class WorldGenHandler
{
	public static final DeferredRegister<Feature<?>> deferredRegister = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
	private static final RegistryObject<Feature<NoneFeatureConfiguration>> limoniteGen = deferredRegister.register(
			"limonite_gen", () -> new SwampFloorOreGenerator((Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f,
					NoneFeatureConfiguration.CODEC));

	// surface blocks
	private final Set<Block> surfaceBlocks = new HashSet<Block>(5)
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
		// flintGenerator = new SurfaceWorldGenerator(FABlocks.flintRock.ToBlock().defaultBlockState(), 5, surfaceBlocks);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void BiomeLoadingEvent(BiomeLoadingEvent event)
	{
		Biome.BiomeCategory category = event.getCategory();
		BiomeGenerationSettingsBuilder biome = event.getGeneration();
		if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND
					&& category != Biome.BiomeCategory.NONE)
		{
			AddOre(biome,
					new OreConfiguration(NATURAL_STONE, FABlocks.metalOres.GetBlock(COPPER).defaultBlockState(), 10),
					CountRange(9, 64));
			AddOre(biome,
					new OreConfiguration(NATURAL_STONE, FABlocks.metalOres.GetBlock(TIN).defaultBlockState(), 4),
					CountRange(15, 64));
			biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(
					new SimpleStateProvider(FABlocks.flintRock.ToBlock().defaultBlockState()),
					new SimpleBlockPlacer()).tries(1).build()).count(1));
			biome.addFeature(VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(
					new SimpleStateProvider(FABlocks.rock.ToBlock().defaultBlockState()), new RockBlockPlacer())
																						 .tries(2).build()).count(5));
		}
		if (category == Biome.BiomeCategory.THEEND)
		{
			AddOre(biome, new OreConfiguration(
					new BlockMatchTest(Blocks.END_STONE),
					FABlocks.siliconQuartzOre.ToBlock().defaultBlockState(), 9), CountRange(2, 255));
		}
		if (category == Biome.BiomeCategory.SWAMP)
		{
			biome.addFeature(UNDERGROUND_ORES, limoniteGen.get().configured(NoneFeatureConfiguration.NONE).chance(17));
		}
	}

	public static void AddFeaturesToBiomes()
	{
		/* TODO: Switch to BiomeLoadingEvent
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			if (biome.getBiomeCategory() != Biome.Category.NETHER && biome.getBiomeCategory() != Biome.Category.THEEND
						&& biome.getBiomeCategory() != Biome.Category.NONE)
			{
				AddOre(biome,
						new OreFeatureConfig(NATURAL_STONE, FABlocks.metalOres.GetBlock(COPPER).defaultBlockState(), 10),
						CountRange(9, 64));
				AddOre(biome,
						new OreFeatureConfig(NATURAL_STONE, FABlocks.metalOres.GetBlock(TIN).defaultBlockState(), 4),
						CountRange(15, 64));
				AddToBiome(biome, Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(FABlocks.flintRock.ToBlock().defaultBlockState()),
								new SimpleBlockPlacer()).tries(1).build(), VEGETAL_DECORATION,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1)));
				AddToBiome(biome, Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
								new SimpleBlockStateProvider(FABlocks.rock.ToBlock().defaultBlockState()), new RockBlockPlacer())
																.tries(2).build(), VEGETAL_DECORATION,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(5)));
			}
			if (biome.getCategory() == Biome.Category.THEEND)
			{
				AddOre(biome, new OreFeatureConfig(OreFeatureConfig.FillerBlockType
														   .create("END_STONE", "end_stone", new BlockMatcher(Blocks.END_STONE)),
						FABlocks.siliconQuartzOre.ToBlock().defaultBlockState(), 9), CountRange(2, 255));
			}
			if (biome.getCategory() == Biome.Category.SWAMP)
			{
				AddToBiome(biome, limoniteGen.get(), NoFeatureConfig.NO_FEATURE_CONFIG, UNDERGROUND_ORES,
						COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2)));
			}
		}
		*/
	}

	private static <T extends FeatureConfiguration> void AddToBiome(BiomeGenerationSettingsBuilder biome, Feature<T> feature, T config,
															  GenerationStep.Decoration stage, ConfiguredDecorator<?> placement)
	{
		biome.addFeature(stage, feature.configured(config).decorated(placement));
	}

	private static void AddOre(BiomeGenerationSettingsBuilder biome, OreConfiguration config, ConfiguredDecorator<?> placement)
	{
		biome.addFeature(UNDERGROUND_ORES, Feature.ORE.configured(config).decorated(placement));
	}

	private static ConfiguredDecorator<?> CountRange(int count, int max)
	{
		return FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(0, 0, max)).count(count);
	}

	//	@Override
	//	public void generate(Random random, int chunkX, int chunkZ, World level, IChunkGenerator chunkGenerator,
	//			IChunkProvider chunkProvider)
	//	{
	//		switch (world.provider.getDimensionType())
	//		{
	//		case OVERWORLD:
	//			RunGenerator(copperGen, level, random, chunkX, chunkZ, 9, 0, 64);
	//			RunGenerator(tinGen, level, random, chunkX, chunkZ, 15, 0, 64);
	//			if (random.nextFloat() < 0.2)
	//				WaterFeatureGenerator(limoniteGen, level, random, chunkX, chunkZ, 1);
	//			RunGenerator(rockGenerator, level, random, chunkX, chunkZ, 2, 50, 100);
	//			RunGenerator(flintGenerator, level, random, chunkX, chunkZ, 1, 50, 100);
	//			break;
	//		case Biomes.THE_END:
	//			RunGenerator(siliconQuartzGen, level, random, chunkX, chunkZ, 2, 0, 255);
	//		default:
	//			break;
	//		}
	//	}
}
