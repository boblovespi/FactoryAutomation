package boblovespi.factoryautomation.common.util.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;
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

	@Override
	public ItemStack[] getMatchingStacks()
	{
		return new ItemStack[] { FluidUtil.getFilledBucket(fluid) };
	}

	@Override
	public boolean test(@Nullable ItemStack stack)
	{
		if (fluid != null && stack != null && fluid.isFluidEqual(stack))
		{
			Optional<FluidStack> f = FluidUtil.getFluidContained(stack);
			if (fluid.getAmount() <= f.orElse(FluidStack.EMPTY).getAmount())
				return true;
		}
		return false;
	}
}
