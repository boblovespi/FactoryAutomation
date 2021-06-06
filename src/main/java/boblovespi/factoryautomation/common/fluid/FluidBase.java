package boblovespi.factoryautomation.common.fluid;

import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

/**
 * Created by Willi on 12/28/2017.
 */
public class FluidBase
{
	public RegistryObject<FlowingFluid> flowing;
	public RegistryObject<FlowingFluid> still;

	public FlowingFluid flowing()
	{
		return flowing.get();
	}

	public FlowingFluid still()
	{
		return still.get();
	}

	public void update(FluidBase other)
	{
		this.flowing = other.flowing;
		this.still = other.still;
	}
}
