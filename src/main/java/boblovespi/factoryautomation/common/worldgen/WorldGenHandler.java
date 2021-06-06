package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import com.mojang.datafixers.util.Pair;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SwampSurfaceBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.item.types.MetalOres.COPPER;
import static boblovespi.factoryautomation.common.item.types.MetalOres.TIN;
import static net.minecraft.world.gen.GenerationStage.Decoration.UNDERGROUND_ORES;
import static net.minecraft.world.gen.GenerationStage.Decoration.VEGETAL_DECORATION;
import static net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD;

/**
 * Created by Willi on 12/28/2017.
 * level generator class
 *
 * @author Willi
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WorldGenHandler
{
    public static final RuleTest FILLER_END_STONE = new RuleTest() {
        @Override
        public boolean test(BlockState state, Random rand) {
            return state == Blocks.END_STONE.getDefaultState();
        }

        @Override
        protected IRuleTestType<?> getType() {
            return IRuleTestType.BLOCK_MATCH;
        }
    };

	public static final DeferredRegister<Feature<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
	// Todo: add custom feature for limonite ore. (I guess it's a custom feature)
//	private static RegistryObject<Feature<NoFeatureConfig>> limoniteGen = DEFERRED_REGISTER.register(
//			"limonite_gen", () -> new Feature<NoFeatureConfig>() {
//                @Override
//                public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
//                    return ;
//                }
//            }.SWAMP.func_242929_a(new SurfaceBuilderConfig(FABlocks.limoniteOre))Ore) FABlocks.limoniteOre, 12, 0.6f, 0.9f, 0.8f,
//					NoFeatureConfig::deserialize));

    /**
     * Loads biome from the biome loading event.
     *
     * @param event the biome loading event from the event subscription.
     * @author Qboi123
     */
    @SubscribeEvent
	public static void onLoadingBiome(BiomeLoadingEvent event) {
        loadIntoBiomes(event);
    }

    public static void init() {

    }

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

    /**
     * Loads features into the biomes from {@link #onLoadingBiome(BiomeLoadingEvent)}
     *
     * @param event biome loading event passed by {@link #onLoadingBiome(BiomeLoadingEvent)}
     * @author Qboi123, Willi
     */
	public static void loadIntoBiomes(BiomeLoadingEvent event) {
        if (!(event.getCategory() == Biome.Category.NETHER || event.getCategory() == Biome.Category.THEEND || event.getCategory() == Biome.Category.NONE)) {
            addOre(event, new OreFeatureConfig(BASE_STONE_OVERWORLD, FABlocks.metalOres.getBlock(COPPER).getDefaultState(), 10), UNDERGROUND_ORES, count(9), range(64));
            addOre(event, new OreFeatureConfig(BASE_STONE_OVERWORLD, FABlocks.metalOres.getBlock(TIN).getDefaultState(), 4), UNDERGROUND_ORES, count(15), range(64));
            addToBiome(event, Feature.RANDOM_PATCH,
                    new BlockClusterFeatureConfig.Builder(
                            new SimpleBlockStateProvider(FABlocks.flintRock.toBlock().getDefaultState()),
                            new SimpleBlockPlacer()).tries(1).build(), VEGETAL_DECORATION,
                    Placement.HEIGHTMAP_SPREAD_DOUBLE.configure(new NoPlacementConfig()),
                    Placement.COUNT.configure(new FeatureSpreadConfig(1)));
            addToBiome(event, Feature.RANDOM_PATCH, new BlockClusterFeatureConfig.Builder( // Todo: maybe use Feature.FOREST_ROCK
                            new SimpleBlockStateProvider(FABlocks.rock.toBlock().getDefaultState()), new SimpleBlockPlacer())
                            .xSpread(2).ySpread(2).zSpread(2)
                            .tries(2).build(), VEGETAL_DECORATION,
                    Placement.HEIGHTMAP_SPREAD_DOUBLE.configure(new NoPlacementConfig()),
                    Placement.COUNT.configure(new FeatureSpreadConfig(5)));
        }
        if (event.getCategory() == Biome.Category.THEEND) {
            addOre(event, new OreFeatureConfig(FILLER_END_STONE, FABlocks.siliconQuartzOre.toBlock().getDefaultState(), 9), UNDERGROUND_ORES, count(2), range(255));
        }
        if (event.getCategory() == Biome.Category.SWAMP)
        {
//            addToBiome(event, limoniteGen.get(), NoFeatureConfig.NO_FEATURE_CONFIG, UNDERGROUND_ORES,
//                    Placement.HEIGHTMAP_SPREAD_DOUBLE.configure(new NoPlacementConfig()),
//                    Placement.COUNT.configure(new FeatureSpreadConfig(2)));
        }
	}

	@Deprecated
	private static <T extends IFeatureConfig> void addToBiome(Biome biome, Feature<T> feature, T config, GenerationStage.Decoration stage, ConfiguredPlacement<?> placement) {

	}

	private static <T extends IFeatureConfig> void addToBiome(BiomeLoadingEvent event, Feature<T> feature, T config, GenerationStage.Decoration stage, ConfiguredPlacement<?>... placements) {
        ConfiguredFeature<?, ?> configuredFeature = feature.withConfiguration(config);
        for (ConfiguredPlacement<?> placement : placements) {
            configuredFeature = configuredFeature.withPlacement(placement);
        }

        event.getGeneration().withFeature(stage, configuredFeature);
	}

	@Deprecated
	private static void addOre(Biome biome, OreFeatureConfig config, GenerationStage.Decoration stage, ConfiguredPlacement<?> placement) {

	}

	private static void addOre(BiomeLoadingEvent event, OreFeatureConfig config, GenerationStage.Decoration stage, ConfiguredPlacement<?>... placements) {
        ConfiguredFeature<?, ?> configuredFeature = Feature.ORE.withConfiguration(config);
        for (ConfiguredPlacement<?> placement : placements) {
            configuredFeature = configuredFeature.withPlacement(placement);
        }

        event.getGeneration().withFeature(stage, configuredFeature);
	}

	private static ConfiguredPlacement<?> count(int count) {
		return Placement.COUNT.configure(new FeatureSpreadConfig(count));
	}

	private static ConfiguredPlacement<?> range(int max) {
		return Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, max));
	}

	private static ConfiguredPlacement<?> range(int min, int max) {
		return Placement.RANGE.configure(new TopSolidRangeConfig(min, 0, max));
	}

	//	@Override
	//	public void generate(Random random, int chunkX, int chunkZ, World level, IChunkGenerator chunkGenerator,
	//			IChunkProvider chunkProvider)
	//	{
	//		switch (level.provider.getDimensionType())
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
