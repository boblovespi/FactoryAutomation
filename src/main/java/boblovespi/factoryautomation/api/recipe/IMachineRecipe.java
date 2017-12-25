package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 12/25/2017.
 */
public interface IMachineRecipe
{
	/**
	 * @return A list of all the inputs
	 */
	List<ItemStack> GetItemInputs();

	/**
	 * @return
	 */
	List<FluidStack> GetFluidInputs();

	/**
	 * @return
	 */
	List<ItemStack> GetPrimaryItemOutputs();

	/**
	 * @return
	 */
	List<FluidStack> GetPrimaryFluidOutputs();

	/**
	 * @return
	 */
	@Nullable
	Map<ItemStack, Float> GetSecondaryItemOutputs();

	/**
	 * @return
	 */
	@Nullable
	Map<FluidStack, Float> GetSecondaryFluidOutputs();
}
