package boblovespi.factoryautomation.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
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
	public static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
	// public static Fluid sodiumChloride = new FluidBase("sodium_chloride", null, null);
	private static final ResourceLocation waterStill = new ResourceLocation("minecraft", "block/water_still");
	private static final ResourceLocation waterFlowing = new ResourceLocation("minecraft", "block/water_flow");

	public static FluidBase steam = new FluidBase();
	public static Properties steamProperties = new Properties(Fluids.steam::Still, Fluids.steam::Flowing,
			FluidAttributes.builder(GetFluidPath("steam_still"), GetFluidPath("steam_flow")).density(0).temperature(373)
						   .gaseous().viscosity(8000));

	public static FluidBase air = new FluidBase();
	public static Properties airProperties = new Properties(Fluids.air::Still, Fluids.air::Flowing,
			FluidAttributes.builder(GetFluidPath("air_still"), GetFluidPath("air_still")).density(0).temperature(300)
						   .gaseous().viscosity(15000));

	public static FluidBase rubberSap = new FluidBase();
	public static Properties rubberSapProperties = new Properties(Fluids.rubberSap::Still, Fluids.rubberSap::Flowing,
			FluidAttributes.builder(GetFluidPath("rubber_sap"), GetFluidPath("rubber_sap")).density(1200)
						   .temperature(300).gaseous().viscosity(40000));

	public static FluidBase moltenNetherMetal = new FluidBase();
	public static Properties moltenNetherMetalProperties = new Properties(Fluids.moltenNetherMetal::Still,
			Fluids.moltenNetherMetal::Flowing,
			FluidAttributes.builder(GetFluidPath("molten_nether_metal"), GetFluidPath("molten_nether_metal"))
						   .density(5000).temperature(550).viscosity(88000));

	public static FluidBase tannin = new FluidBase();
	public static Properties tanninProperties = new Properties(Fluids.tannin::Still, Fluids.tannin::Flowing, FluidAttributes.builder(waterStill, waterFlowing)
			.density(1000).temperature(300).viscosity(1000).color(0xFF6E4434));

	public static FluidBase limewater = new FluidBase();
	public static Properties limewaterProperties = new Properties(Fluids.limewater::Still, Fluids.limewater::Flowing, FluidAttributes.builder(waterStill, waterFlowing)
			.density(1000).temperature(300).viscosity(1000).color(0xFFF3F0DE));

	static
	{
		steam.Update(MakeAndRegister("fa_steam", Fluids.steamProperties));
		air.Update(MakeAndRegister("fa_air", Fluids.airProperties));
		rubberSap.Update(MakeAndRegister("rubber_sap", Fluids.rubberSapProperties));
		moltenNetherMetal.Update(MakeAndRegister("molten_nether_metal", Fluids.moltenNetherMetalProperties));
		tannin.Update(MakeAndRegister("tannin", Fluids.tanninProperties));
		limewater.Update(MakeAndRegister("limewater", Fluids.limewaterProperties));
	}
	private static final List<Fluid> fluids = new ArrayList<>();

	//	static
	//	{
	//		steam.setBlock(new FluidFinite(steam, Material.WATER, "steam"));
	//	}

	private static ResourceLocation GetFluidPath(String location)
	{
		return new ResourceLocation(MODID, "fluids/" + location);
	}

	private static FluidBase MakeAndRegister(String name, Properties properties)
	{
		FluidBase base = new FluidBase();
		base.flowing = FLUID_REGISTER.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(properties));
		base.still = FLUID_REGISTER.register(name + "_still", () -> new ForgeFlowingFluid.Source(properties));
		return base;
	}
}
