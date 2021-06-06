package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 12/25/2017.
 */
public abstract class ChancelessMachineRecipe implements IMachineRecipe
{
	protected List<Ingredient> itemInputs;
	protected List<FluidStack> fluidInputs;
	protected List<ItemStack> itemOutputs;
	protected List<FluidStack> fluidOutputs;

	public ChancelessMachineRecipe(List<Ingredient> itemInputs,
			List<FluidStack> fluidInputs, List<ItemStack> itemOutputs,
			List<FluidStack> fluidOutputs)
	{
		this.itemInputs = itemInputs;
		this.fluidInputs = fluidInputs;
		this.itemOutputs = itemOutputs;
		this.fluidOutputs = fluidOutputs;
	}

	/**
	 * @return A list of all the item inputs
	 */
	@Override
	public List<Ingredient> GetItemInputs()
	{
		return itemInputs;
	}

	/**
	 * @return A list of all the fluid inputs
	 */
	@Override
	public List<FluidStack> GetFluidInputs()
	{
		return fluidInputs;
	}

	/**
	 * @return A list of all the item outputs with a 100% chance of being produced
	 */
	@Override
	public List<ItemStack> GetPrimaryItemOutputs()
	{
		return itemOutputs;
	}

	/**
	 * @return A list of all the fluid outputs with a 100% chance of being produced
	 */
	@Override
	public List<FluidStack> GetPrimaryFluidOutputs()
	{
		return fluidOutputs;
	}

	/**
	 * @return A list of all the item outputs with chances. Null if there are none
	 */
	@Nullable
	@Override
	public Map<ItemStack, Float> GetSecondaryItemOutputs()
	{
		return null;
	}

	/**
	 * @return A list of all the fluid outputs with chances. Null if there are none
	 */
	@Nullable
	@Override
	public Map<FluidStack, Float> GetSecondaryFluidOutputs()
	{
		return null;
	}

	@Override
	public void writeToNBT(CompoundNBT tag)
	{
		// itemInputs.stream().forEach(n -> n.w);

	}
}
