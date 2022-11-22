package boblovespi.factoryautomation.common.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class PlacedFeatures
{
	//	public static PlacedFeature flintPatchNormal = Register("patch_flint_normal", ConfiguredFeatures.flintPatch.placed(VegetationPlacements.worldSurfaceSquaredWithCount(1)));
	//	public static PlacedFeature rockPatchNormal = Register("patch_rock_normal", ConfiguredFeatures.rockPatch.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	//	public static PlacedFeature rockPatchDesert = Register("patch_rock_desert", ConfiguredFeatures.rockPatchDesert.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	//	public static PlacedFeature rockPatchMesa = Register("patch_rock_mesa", ConfiguredFeatures.rockPatchMesa.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	//	public static PlacedFeature rockPatchSwamp = Register("patch_rock_swamp", ConfiguredFeatures.rockPatchSwamp.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	//	public static PlacedFeature tinSmallVeinNormal = Register("ore_cassiterite_small_normal", ConfiguredFeatures.tinSmallVein.placed(SmallOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.absolute(64 - 32), VerticalAnchor.absolute(64 + 32)))));
	//	public static PlacedFeature limoniteVeinNormal = Register("ore_limonite_normal", ConfiguredFeatures.limoniteVein.placed(LargeOrePlacement(17, PlacementUtils.HEIGHTMAP_TOP_SOLID)));
	//	public static PlacedFeature blackSandNormal = Register("ore_black_sand_normal", ConfiguredFeatures.blackSand.placed(LargeOrePlacement(200, PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));

	public static void init()
	{

	}

	private static PlacedFeature Register(String name, PlacedFeature feature)
	{
		return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, name), feature);
	}

	private static List<PlacementModifier> SmallOrePlacement(int count, PlacementModifier modifier)
	{
		return OrePlacement(CountPlacement.of(count), modifier);
	}

	private static List<PlacementModifier> LargeOrePlacement(int frequency, PlacementModifier modifier) {
		return OrePlacement(RarityFilter.onAverageOnceEvery(frequency), modifier);
	}

	private static List<PlacementModifier> OrePlacement(PlacementModifier modifier, PlacementModifier modifier2)
	{
		return List.of(modifier, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
	}
}
