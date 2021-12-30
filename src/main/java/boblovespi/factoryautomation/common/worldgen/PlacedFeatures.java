package boblovespi.factoryautomation.common.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class PlacedFeatures
{
	public static PlacedFeature flintPatchNormal = Register("patch_flint_normal", ConfiguredFeatures.flintPatch.placed(VegetationPlacements.worldSurfaceSquaredWithCount(1)));
	public static PlacedFeature rockPatchNormal = Register("patch_rock_normal", ConfiguredFeatures.rockPatch.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	public static PlacedFeature rockPatchDesert = Register("patch_rock_desert", ConfiguredFeatures.rockPatchDesert.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	public static PlacedFeature rockPatchMesa = Register("patch_rock_mesa", ConfiguredFeatures.rockPatchMesa.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));
	public static PlacedFeature rockPatchSwamp = Register("patch_rock_swamp", ConfiguredFeatures.rockPatchSwamp.placed(VegetationPlacements.worldSurfaceSquaredWithCount(2)));


	public static void init()
	{

	}

	private static PlacedFeature Register(String name, PlacedFeature feature)
	{
		return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, name), feature);
	}
}
