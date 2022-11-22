package boblovespi.factoryautomation.common.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/29/2017.
 * TODO: figure out fluids
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Fluids
{
	public static final DeferredRegister<FluidType> FLUID_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MODID);
	public static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
	// public static Fluid sodiumChloride = new FluidBase("sodium_chloride", null, null);
	private static final ResourceLocation waterStill = new ResourceLocation("minecraft", "block/water_still");
	private static final ResourceLocation waterFlowing = new ResourceLocation("minecraft", "block/water_flow");

	private static final List<Fluid> fluids = new ArrayList<>();

	public static FluidType steamType = MakeType(Prop().density(0).temperature(373).viscosity(8000), "steam_still", "steam_flowing");
	public static FluidBase steam = new FluidBase();
	public static Properties steamProperties = MakeProp(steamType, steam);

	public static FluidType airType = MakeType(Prop().density(0).temperature(300).viscosity(15000), "air_still");
	public static FluidBase air = new FluidBase();
	public static Properties airProperties = MakeProp(airType, air);

	public static FluidType rubberSapType = MakeType(Prop().density(1200).temperature(300).viscosity(40000), "rubber_sap");
	public static FluidBase rubberSap = new FluidBase();
	public static Properties rubberSapProperties = MakeProp(rubberSapType, rubberSap);

	public static FluidType moltenNetherMetalType = MakeType(Prop().density(5000).temperature(550).viscosity(88000), "molten_nether_metal");
	public static FluidBase moltenNetherMetal = new FluidBase();
	public static Properties moltenNetherMetalProperties = MakeProp(moltenNetherMetalType, moltenNetherMetal);

	public static FluidType tanninType = MakeType(Prop().density(1000).temperature(300).viscosity(1000), "tannin", 0xFF6E4434);
	public static FluidBase tannin = new FluidBase();
	public static Properties tanninProperties = MakeProp(tanninType, tannin);

	public static FluidType limewaterType = MakeType(Prop().density(1000).temperature(300).viscosity(1000), "limewater", 0xFFF3F0DE);
	public static FluidBase limewater = new FluidBase();
	public static Properties limewaterProperties = MakeProp(limewaterType, limewater);

	static
	{
		steam.Update(MakeAndRegister("steam", Fluids.steamProperties));
		air.Update(MakeAndRegister("air", Fluids.airProperties));
		rubberSap.Update(MakeAndRegister("rubber_sap", Fluids.rubberSapProperties));
		moltenNetherMetal.Update(MakeAndRegister("molten_nether_metal", Fluids.moltenNetherMetalProperties));
		tannin.Update(MakeAndRegister("tannin", Fluids.tanninProperties));
		limewater.Update(MakeAndRegister("limewater", Fluids.limewaterProperties));
	}

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

	private static Properties MakeProp(FluidType type, FluidBase fluid)
	{
		return new Properties(() -> type, fluid::Still, fluid::Flowing);
	}

	private static FluidType MakeType(FluidType.Properties prop, String loc)
	{
		var type = new FAFluidType(prop, new DefaultFluidTexture(loc));
		FLUID_TYPE_REGISTER.register(loc, () -> type);
		return type;
	}

	private static FluidType MakeType(FluidType.Properties prop, String still, String flowing)
	{
		var type = new FAFluidType(prop, new DefaultFluidTexture(still, flowing));
		FLUID_TYPE_REGISTER.register(still, () -> type);
		return type;
	}

	private static FluidType MakeType(FluidType.Properties prop, String name, int tint)
	{
		var type = new FAFluidType(prop, new TintedWater(tint));
		FLUID_TYPE_REGISTER.register(name, () -> type);
		return type;
	}

	private static FluidType.Properties Prop()
	{
		return FluidType.Properties.create();
	}

	private static class FAFluidType extends FluidType
	{
		private final IClientFluidTypeExtensions clientData;

		public FAFluidType(Properties properties, IClientFluidTypeExtensions clientData)
		{
			super(properties);
			this.clientData = clientData;
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(clientData);
		}
	}

	private static class DefaultFluidTexture implements IClientFluidTypeExtensions
	{
		private final ResourceLocation still;
		private final ResourceLocation flowing;

		public DefaultFluidTexture(String loc)
		{
			still = GetFluidPath(loc);
			flowing = still;
		}

		public DefaultFluidTexture(String stillPath, String flowingPath)
		{
			still = GetFluidPath(stillPath);
			flowing = GetFluidPath(flowingPath);
		}

		@Override
		public ResourceLocation getStillTexture()
		{
			return still;
		}

		@Override
		public ResourceLocation getFlowingTexture()
		{
			return flowing;
		}
	}

	private static class TintedWater implements IClientFluidTypeExtensions
	{
		private final int tint;

		public TintedWater(int tint)
		{
			this.tint = tint;
		}

		@Override
		public ResourceLocation getStillTexture()
		{
			return waterStill;
		}

		@Override
		public ResourceLocation getFlowingTexture()
		{
			return waterFlowing;
		}

		@Override
		public int getTintColor()
		{
			return tint;
		}
	}
}
