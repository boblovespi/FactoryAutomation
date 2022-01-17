package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Ore;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.resource.Rock.Variants;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class ConfiguredFeatures
{
	public static ConfiguredFeature<?, ?> flintPatch = Register("patch_flint", Feature.RANDOM_PATCH.configured(SimplePatch(2, SimpleStateProvider.simple(FABlocks.flintRock.ToBlock()))));
	public static ConfiguredFeature<?, ?> rockPatch = Register("patch_rock", Feature.RANDOM_PATCH.configured(SimplePatch(5, new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
																																							  .add(RockVariant(Variants.COBBLESTONE), 7)
																																							  .add(RockVariant(Variants.STONE), 7)
																																							  .add(RockVariant(Variants.DIORITE), 5)
																																							  .add(RockVariant(Variants.ANDESITE), 5)
																																							  .add(RockVariant(Variants.GRANITE), 5)
																																							  .add(RockVariant(Variants.TUFF), 2)
																																							  .add(RockVariant(Variants.CALCITE), 2)))));
	public static ConfiguredFeature<?, ?> rockPatchDesert = Register("patch_rock_desert", Feature.RANDOM_PATCH.configured(SimplePatch(4, SimpleStateProvider.simple(RockVariant(Variants.SANDSTONE)))));
	public static ConfiguredFeature<?, ?> rockPatchMesa = Register("patch_rock_mesa", Feature.RANDOM_PATCH.configured(SimplePatch(4, SimpleStateProvider.simple(RockVariant(Variants.TERRACOTTA)))));
	public static ConfiguredFeature<?, ?> rockPatchSwamp = Register("patch_rock_swamp", Feature.RANDOM_PATCH.configured(SimplePatch(5, SimpleStateProvider.simple(RockVariant(Variants.MOSSY_COBBLESTONE)))));
	public static ConfiguredFeature<?, ?> tinSmallVein = Register("ore_cassiterite_small", Feature.ORE.configured(new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, FABlocks.metalOres.GetBlock(MetalOres.TIN)
			.ToBlock().defaultBlockState(), 4)));
	public static ConfiguredFeature<?, ?> limoniteVein = Register("ore_limonite", WorldGenHandler.limoniteGen.get().configured(NoneFeatureConfiguration.INSTANCE));
	public static ConfiguredFeature<?, ?> blackSand = Register("ore_black_sand", WorldGenHandler.blackSandGen.get().configured(NoneFeatureConfiguration.INSTANCE));

	public static void init()
	{

	}

	private static ConfiguredFeature<?, ?> Register(String name, ConfiguredFeature<?, ?> feature)
	{
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, name), feature);
	}

	private static PlacedFeature SimpleBlock(BlockStateProvider provider)
	{
		return Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(provider)).onlyWhenEmpty();
	}

	private static RandomPatchConfiguration SimplePatch(int tries, BlockStateProvider provider)
	{
		return FeatureUtils.simpleRandomPatchConfiguration(tries, SimpleBlock(provider));
	}

	private static BlockState RockVariant(Variants variant)
	{
		return FABlocks.rock.ToBlock().defaultBlockState().setValue(Rock.VARIANTS, variant);
	}
}
