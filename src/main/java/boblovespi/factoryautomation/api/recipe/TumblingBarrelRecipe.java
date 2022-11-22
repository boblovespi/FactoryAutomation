package boblovespi.factoryautomation.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class TumblingBarrelRecipe implements IMachineRecipe
{
	public static final TumblingBarrelRecipe.Serializer SERIALIZER = new TumblingBarrelRecipe.Serializer();
	public static final RecipeType<TumblingBarrelRecipe> TYPE = RecipeType.simple(
			new ResourceLocation(MODID, "tumbling_barrel"));

	private Ingredient input;
	private ItemStack output;
	private FluidStack inputFluid;
	private FluidStack outputFluid;
	private int time;
	private float minSpeed;
	private float maxSpeed;
	private ResourceLocation name;

	public TumblingBarrelRecipe(String name, Ingredient input, ItemStack output, FluidStack inputFluid,
								FluidStack outputFluid, int time, float minSpeed, float maxSpeed)
	{
		this.input = input;
		this.output = output;
		this.inputFluid = inputFluid;
		this.outputFluid = outputFluid;
		this.time = time;
		this.name = new ResourceLocation(name);
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public List<Ingredient> GetItemInputs()
	{
		return List.of(input);
	}

	@Override
	public List<FluidStack> GetFluidInputs()
	{
		return List.of(inputFluid.copy());
	}

	@Override
	public List<ItemStack> GetPrimaryItemOutputs()
	{
		return List.of(output.copy());
	}

	@Override
	public List<FluidStack> GetPrimaryFluidOutputs()
	{
		return List.of(outputFluid.copy());
	}

	@Nullable
	@Override
	public Map<ItemStack, Float> GetSecondaryItemOutputs()
	{
		return null;
	}

	@Nullable
	@Override
	public Map<FluidStack, Float> GetSecondaryFluidOutputs()
	{
		return null;
	}

	@Override
	public void WriteToNBT(CompoundTag tag)
	{

	}

	@Override
	public ResourceLocation getId()
	{
		return name;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType()
	{
		return TYPE;
	}

	public String GetName()
	{
		return name.toString();
	}

	public Ingredient GetInput()
	{
		return input;
	}

	public int GetTime()
	{
		return time;
	}

	public float GetMinSpeed()
	{
		return minSpeed;
	}

	public float GetMaxSpeed()
	{
		return maxSpeed;
	}

	public static class Serializer implements RecipeSerializer<TumblingBarrelRecipe>
	{
		@Override
		public TumblingBarrelRecipe fromJson(ResourceLocation name, JsonObject json)
		{
			JsonElement element = GsonHelper.isArrayNode(json, "input") ? GsonHelper.getAsJsonArray(json, "input") : GsonHelper.getAsJsonObject(json, "input");
			Ingredient input = Ingredient.fromJson(element);
			ItemStack output = RecipeHelper.GetStackOrEmptyFromObject(json, "output");
			FluidStack inputFluid = RecipeHelper.GetFluidOrEmptyFromObject(json, "inputFluid");
			FluidStack outputFluid = RecipeHelper.GetFluidOrEmptyFromObject(json, "outputFluid");
			int time = GsonHelper.getAsInt(json, "time");
			var minSpeed = GsonHelper.getAsFloat(json, "minSpeed");
			var maxSpeed = GsonHelper.getAsFloat(json, "maxSpeed");
			return new TumblingBarrelRecipe(name.toString(), input, output, inputFluid, outputFluid, time, minSpeed, maxSpeed);
		}

		@Nullable
		@Override
		public TumblingBarrelRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer)
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			FluidStack inputFluid = FluidStack.readFromPacket(buffer);
			FluidStack outputFluid = FluidStack.readFromPacket(buffer);
			int time = buffer.readVarInt();
			var minSpeed = buffer.readFloat();
			var maxSpeed = buffer.readFloat();
			return new TumblingBarrelRecipe(name.toString(), input, output, inputFluid, outputFluid, time, minSpeed, maxSpeed);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, TumblingBarrelRecipe recipe)
		{
			recipe.input.toNetwork(buffer);
			buffer.writeItem(recipe.output);
			recipe.inputFluid.writeToPacket(buffer);
			recipe.outputFluid.writeToPacket(buffer);
			buffer.writeVarInt(recipe.time);
			buffer.writeFloat(recipe.minSpeed);
			buffer.writeFloat(recipe.maxSpeed);
		}
	}
}
