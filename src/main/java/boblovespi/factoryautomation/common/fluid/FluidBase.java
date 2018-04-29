package boblovespi.factoryautomation.common.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.util.function.Consumer;

/**
 * Created by Willi on 12/28/2017.
 */
public class FluidBase extends Fluid
{
	public FluidBase(String fluidName, ResourceLocation still,
			ResourceLocation flowing)
	{
		super(fluidName, still, flowing);
	}

	public FluidBase Setup(Consumer<FluidBase> apply)
	{
		apply.accept(this);
		return this;
	}
}
