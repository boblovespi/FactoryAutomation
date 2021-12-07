package boblovespi.factoryautomation.common.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

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
