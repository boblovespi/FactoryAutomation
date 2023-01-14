package boblovespi.factoryautomation.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class BrickMakerRecipe extends ChancelessMachineRecipe
{
	public static final Serializer SERIALIZER = new Serializer();
	public static final RecipeType<BrickMakerRecipe> TYPE = RecipeType.simple(
			new ResourceLocation(MODID, "brick_maker"));
	private final ResourceLocation name;
	private final Ingredient input;
	private final int count;
	private final ItemStack output;
	private final int time;
	private final Block render;

	public BrickMakerRecipe(ResourceLocation name, Ingredient input, int count, ItemStack output, int time, Block render)
	{
		super(List.of(input), null, List.of(output), null);
		this.name = name;
		this.input = input;
		this.count = count;
		this.output = output;
		this.time = time;
		this.render = render;
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

	public static class Serializer implements RecipeSerializer<BrickMakerRecipe>
	{
		@Override
		public BrickMakerRecipe fromJson(ResourceLocation name, JsonObject json)
		{
			JsonElement element = GsonHelper.isArrayNode(json, "input") ? GsonHelper.getAsJsonArray(json, "input") : GsonHelper.getAsJsonObject(json, "input");
			Ingredient input = Ingredient.fromJson(element);
			int count = GsonHelper.getAsInt(json, "count");
			ItemStack output = RecipeHelper.GetItemStackFromObject(json, "output");
			int time = GsonHelper.getAsInt(json, "time");
			Block render = RecipeHelper.GetBlockFromObject(json, "render");
			return new BrickMakerRecipe(name, input, count, output, time, render);
		}

		@Override
		public @Nullable BrickMakerRecipe fromNetwork(ResourceLocation name, FriendlyByteBuf buffer)
		{
			var input = Ingredient.fromNetwork(buffer);
			var count = buffer.readVarInt();
			var output = buffer.readItem();
			var time = buffer.readVarInt();
			var render = ForgeRegistries.BLOCKS.getValue(buffer.readResourceLocation());
			return new BrickMakerRecipe(name, input, count, output, time, render);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BrickMakerRecipe recipe)
		{
			recipe.input.toNetwork(buffer);
			buffer.writeVarInt(recipe.count);
			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.time);
			buffer.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(recipe.render));
		}
	}
}
