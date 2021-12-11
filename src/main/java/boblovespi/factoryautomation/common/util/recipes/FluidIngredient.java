package boblovespi.factoryautomation.common.util.recipes;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.stream.Stream;

/**
 * Created by Willi on 7/1/2018.
 */
public class FluidIngredient extends Ingredient
{
	private FluidStack fluid;

	public FluidIngredient(FluidStack fluid)
	{
		super(Stream.empty());
		this.fluid = fluid;
	}

	/*@Override
	public ItemStack[] getMatchingStacks()
	{
		return new ItemStack[] { FluidUtil.getFilledBucket(fluid) };
	}

	@Override
	public boolean test(@Nullable ItemStack stack)
	{
		if (fluid != null && stack != null && fluid.isFluidEqual(stack))
		{
			LazyOptional<FluidStack> f = FluidUtil.getFluidContained(stack);
			if (fluid.getAmount() <= f.orElse(FluidStack.EMPTY).getAmount())
				return true;
		}
		return false;
	}*/
}
