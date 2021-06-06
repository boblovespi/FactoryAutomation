package boblovespi.factoryautomation.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/29/2017.
 * TODO: figure out fluids
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Fluids
{
	public static final DeferredRegister<Fluid> FLUID_REGISTER = new DeferredRegister<>(ForgeRegistries.FLUIDS, MODID);
	// public static Fluid sodiumChloride = new FluidBase("sodium_chloride", null, null);

	public static FluidBase steam = new FluidBase();
	public static Properties steamProperties = new Properties(Fluids.steam::still, Fluids.steam::flowing,
			FluidAttributes.builder(getFluidPath("steam_still"), getFluidPath("steam_flow")).density(0).temperature(373)
						   .gaseous().viscosity(8000));

	public static FluidBase air = new FluidBase();
	public static Properties airProperties = new Properties(Fluids.air::still, Fluids.air::flowing,
			FluidAttributes.builder(getFluidPath("air_still"), getFluidPath("air_still")).density(0).temperature(300)
						   .gaseous().viscosity(15000));

	public static FluidBase rubberSap = new FluidBase();
	public static Properties rubberSapProperties = new Properties(Fluids.rubberSap::still, Fluids.rubberSap::flowing,
			FluidAttributes.builder(getFluidPath("rubber_sap"), getFluidPath("rubber_sap")).density(1200)
						   .temperature(300).gaseous().viscosity(40000));

	public static FluidBase moltenNetherMetal = new FluidBase();
	public static Properties moltenNetherMetalProperties = new Properties(Fluids.moltenNetherMetal::still,
			Fluids.moltenNetherMetal::flowing,
			FluidAttributes.builder(getFluidPath("molten_nether_metal"), getFluidPath("molten_nether_metal"))
						   .density(5000).temperature(550).viscosity(88000));

	static
	{
		steam.update(makeAndRegister("fa_steam", Fluids.steamProperties));
		air.update(makeAndRegister("fa_air", Fluids.airProperties));
		rubberSap.update(makeAndRegister("rubber_sap", Fluids.rubberSapProperties));
		moltenNetherMetal.update(makeAndRegister("molten_nether_metal", Fluids.moltenNetherMetalProperties));
	}
	private static final List<Fluid> fluids = new ArrayList<>();

	//	static
	//	{
	//		steam.setBlock(new FluidFinite(steam, Material.WATER, "steam"));
	//	}

	private static ResourceLocation getFluidPath(String location)
	{
		return new ResourceLocation(MODID, "fluids/" + location);
	}

	private static FluidBase makeAndRegister(String name, Properties properties)
	{
		FluidBase base = new FluidBase();
		base.flowing = FLUID_REGISTER.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(properties));
		base.still = FLUID_REGISTER.register(name + "_still", () -> new ForgeFlowingFluid.Source(properties));
		return base;
	}
}
