package boblovespi.factoryautomation.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by Willi on 12/25/2017.
 */
public interface IMachineRecipe extends Recipe<Container>
{
	/**
	 * @return A list of all the item inputs
	 */
	List<Ingredient> GetItemInputs();

	/**
	 * @return A list of all the fluid inputs
	 */
	default List<FluidStack> GetFluidInputs()
	{
		return null;
	}

	/**
	 * @return A list of all the item outputs with a 100% chance of being produced
	 */
	List<ItemStack> GetPrimaryItemOutputs();

	/**
	 * @return A list of all the fluid outputs with a 100% chance of being produced
	 */
	default List<FluidStack> GetPrimaryFluidOutputs()
	{
		return null;
	}

	/**
	 * @return A list of all the item outputs with chances. Null if there are none
	 */
	@Nullable
	default Map<ItemStack, Float> GetSecondaryItemOutputs()
	{
		return null;
	}

	/**
	 * @return A list of all the fluid outputs with chances. Null if there are none
	 */
	@Nullable
	default Map<FluidStack, Float> GetSecondaryFluidOutputs()
	{
		return null;
	}

	void WriteToNBT(CompoundTag tag);

	@Override
	default boolean matches(Container inv, Level world)
	{
		return false;
	}

	@Override
	default ItemStack assemble(Container inv)
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
	RecipeSerializer<?> getSerializer();

	@Override
	RecipeType<?> getType();
}
