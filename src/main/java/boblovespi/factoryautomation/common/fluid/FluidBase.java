package boblovespi.factoryautomation.common.fluid;

import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

/**
 * Created by Willi on 12/28/2017.
 */
public class FluidBase
{
	public RegistryObject<FlowingFluid> flowing;
	public RegistryObject<FlowingFluid> still;

	public FlowingFluid Flowing()
	{
		return flowing.get();
	}

	public FlowingFluid Still()
	{
		return still.get();
	}

	public void Update(FluidBase other)
	{
		this.flowing = other.flowing;
		this.still = other.still;
	}
}
