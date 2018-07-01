package boblovespi.factoryautomation.common.util.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

/**
 * Created by Willi on 7/1/2018.
 */
public class FluidIngredient extends Ingredient
{
	private FluidStack fluid;

	public FluidIngredient(FluidStack fluid)
	{
		this.fluid = fluid;
	}

	@Override
	public ItemStack[] getMatchingStacks()
	{
		return new ItemStack[] { FluidUtil.getFilledBucket(fluid) };
	}

	@Override
	public boolean apply(@Nullable ItemStack stack)
	{
		if (fluid != null && stack != null && fluid.isFluidEqual(stack))
		{
			FluidStack f = FluidUtil.getFluidContained(stack);
			if (f != null && fluid.amount <= f.amount)
			{
				return true;
			}
		}
		return false;
	}
}
