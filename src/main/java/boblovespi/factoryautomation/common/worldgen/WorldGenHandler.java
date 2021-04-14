package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
import static net.minecraft.world.gen.feature.Features.Placements.PATCH_PLACEMENT;
import static net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD;

/**
 * Created by Willi on 12/28/2017.
 * world generator class
 */
@Mod.EventBusSubscriber(modid = MODID)
public class WorldGenHandler
{
	public static final DeferredRegister<Feature<?>> deferredRegister = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
	private static RegistryObject<Feature<NoFeatureConfig>> limoniteGen = deferredRegister.register(
			"limonite_gen", () -> new SwampFloorOreGenerator((Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f,
					NoFeatureConfig.field_236558_a_));

	private static TagMatchRuleTest endstone = new TagMatchRuleTest(FATags.ForgeBlockTag("end_stones"));

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

	@SubscribeEvent
	public static void AddFeaturesToBiomes(BiomeLoadingEvent event)
	{
		if (event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND
				&& event.getCategory() != Biome.Category.NONE)
		{
			AddOre(event.getGeneration(), new OreFeatureConfig(BASE_STONE_OVERWORLD, FABlocks.metalOres.GetBlock(COPPER).getDefaultState(), 10), CountRange(9, 64));
			AddOre(event.getGeneration(), new OreFeatureConfig(BASE_STONE_OVERWORLD, FABlocks.metalOres.GetBlock(TIN).getDefaultState(), 4), CountRange(15, 64));
			AddToBiome(event.getGeneration(), Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
							new SimpleBlockStateProvider(FABlocks.flintRock.ToBlock().getDefaultState()),
							new SimpleBlockPlacer()).tries(1).build(), VEGETAL_DECORATION,
					PATCH_PLACEMENT.chance(1));
			AddToBiome(event.getGeneration(), Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder(
							new SimpleBlockStateProvider(FABlocks.rock.ToBlock().getDefaultState()), new RockBlockPlacer())
							.tries(2).build(), VEGETAL_DECORATION,
					PATCH_PLACEMENT.chance(5));
		}
		if (event.getCategory() == Biome.Category.THEEND)
		{
			AddOre(event.getGeneration(), new OreFeatureConfig(endstone,
					FABlocks.siliconQuartzOre.ToBlock().getDefaultState(), 9), CountRange(2, 255));
		}
		if (event.getCategory() == Biome.Category.SWAMP)
		{
			AddToBiome(event.getGeneration(), limoniteGen.get(), NoFeatureConfig.NO_FEATURE_CONFIG, UNDERGROUND_ORES,
					PATCH_PLACEMENT.chance(2));
		}
	}

	private static <T extends IFeatureConfig> void AddToBiome(BiomeGenerationSettings.Builder biome, Feature<T> feature, T config,
															  GenerationStage.Decoration stage, ConfiguredPlacement<?> placement)
	{
		biome.withFeature(stage, feature.withConfiguration(config).withPlacement(placement));
	}

	private static void AddOre(BiomeGenerationSettings.Builder biome, OreFeatureConfig config, ConfiguredPlacement<?> placement)
	{
		biome.withFeature(UNDERGROUND_ORES, Feature.ORE.withConfiguration(config).withPlacement(placement));
	}

	private static ConfiguredPlacement<?> CountRange(int count, int max)
	{
		return Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, max)).withPlacement(Placement.COUNT.configure(new FeatureSpreadConfig(count)));
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
