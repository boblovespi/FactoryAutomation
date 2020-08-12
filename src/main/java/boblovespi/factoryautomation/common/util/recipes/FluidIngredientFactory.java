package boblovespi.factoryautomation.common.util.recipes;

import boblovespi.factoryautomation.common.util.Constants;
import boblovespi.factoryautomation.common.util.Log;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

/**
 * Created by Willi on 7/1/2018.
 */
@SuppressWarnings("unused")
public class FluidIngredientFactory
{
	//	@Nonnull
	//	public Ingredient parse(JsonContext context, JsonObject json)
	//	{
	//		String name = json.get("fluid").getAsString();
	//		int amount = json.get("amount").getAsInt();
	//
	//		FluidStack fluidStack = FluidRegistry.getFluidStack(name, amount);
	//		if (fluidStack == null)
	//		{
	//			if (Constants.SHOULD_THROW_JSON_EXCEPTIONS)
	//			{
	//				throw new JsonSyntaxException("Fluid [" + name + "] does not exist!");
	//			} else
	//			{
	//				Log.LogError("Fluid [" + name + "] does not exist!");
	//			}
	//		}
	//		return new FluidIngredient(fluidStack);
	//	}
}
