package boblovespi.factoryautomation.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/29/2017.
 */
public class Fluids
{
	public static Fluid sodiumChloride = new FluidBase("sodium_chloride", null, null);
	public static Fluid steam = new FluidBase("fa_steam", new ResourceLocation(MODID, "fluids/steam_still"),
			new ResourceLocation(MODID, "fluids/steam_flow"))
			.Setup(n -> n.setDensity(0).setTemperature(373).setGaseous(true).setViscosity(8000));
	public static Fluid air = new FluidBase(
			"fa_air", new ResourceLocation(MODID, "fluids/air_still"), new ResourceLocation(MODID, "fluids/air_still"))
			.Setup(n -> n.setDensity(0).setGaseous(true).setTemperature(300).setViscosity(15000));
	public static Fluid rubberSap = new FluidBase("rubber_sap", new ResourceLocation(MODID, "fluids/rubber_sap"),
			new ResourceLocation(MODID, "fluids/rubber_sap"))
			.Setup(n -> n.setDensity(1200).setGaseous(false).setTemperature(300).setViscosity(40000));

	//	static
	//	{
	//		steam.setBlock(new FluidFinite(steam, Material.WATER, "steam"));
	//	}

	public static void RegisterFluids()
	{
		RegisterFluid(steam);
		RegisterFluid(air);
		RegisterFluid(rubberSap);
		// TEMP
		FluidRegistry.addBucketForFluid(rubberSap);
		FluidRegistry.addBucketForFluid(steam);
	}

	private static void RegisterFluid(Fluid fluid)
	{
		if (!FluidRegistry.registerFluid(fluid))
		{
			fluid = FluidRegistry.getFluid(fluid.getName());
		}
	}
}
