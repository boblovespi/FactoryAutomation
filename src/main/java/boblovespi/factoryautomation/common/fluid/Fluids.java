package boblovespi.factoryautomation.common.fluid;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by Willi on 12/29/2017.
 */
public class Fluids
{
	public static Fluid sodiumChloride = new FluidBase("sodium_chloride", null, null);
	public static Fluid steam = new FluidBase("fa_steam",
			new ResourceLocation(FactoryAutomation.MODID, "fluids/steam_still"),
			new ResourceLocation(FactoryAutomation.MODID, "fluids/steam_flow"))
			.Setup(n -> n.setDensity(0).setTemperature(373).setGaseous(true).setViscosity(3000));

	//	static
	//	{
	//		steam.setBlock(new FluidFinite(steam, Material.WATER, "steam"));
	//	}

	public static void RegisterFluids()
	{
		RegisterFluid(steam);
		// TEMP
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
