package boblovespi.factoryautomation.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
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
	 * @return A list of all the item inputs
	 */
	List<Ingredient> GetItemInputs();

	/**
	 * @return A list of all the fluid inputs
	 */
	List<FluidStack> GetFluidInputs();

	/**
	 * @return A list of all the item outputs with a 100% chance of being produced
	 */
	List<ItemStack> GetPrimaryItemOutputs();

	/**
	 * @return A list of all the fluid outputs with a 100% chance of being produced
	 */
	List<FluidStack> GetPrimaryFluidOutputs();

	/**
	 * @return A list of all the item outputs with chances. Null if there are none
	 */
	@Nullable
	Map<ItemStack, Float> GetSecondaryItemOutputs();

	/**
	 * @return A list of all the fluid outputs with chances. Null if there are none
	 */
	@Nullable
	Map<FluidStack, Float> GetSecondaryFluidOutputs();

	void WriteToNBT(NBTTagCompound tag);
}
