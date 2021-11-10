package boblovespi.factoryautomation.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 12/25/2017.
 */
public interface IMachineRecipe extends IRecipe<IInventory>
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

	void WriteToNBT(CompoundNBT tag);

	@Override
	default boolean matches(IInventory inv, World world)
	{
		return false;
	}

	@Override
	default ItemStack assemble(IInventory inv)
	{
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_)
	{
		return false;
	}

	@Override
	default ItemStack getResultItem()
	{
		return ItemStack.EMPTY;
	}

	@Override
	ResourceLocation getId();

	@Override
	IRecipeSerializer<?> getSerializer();

	@Override
	IRecipeType<?> getType();
}
