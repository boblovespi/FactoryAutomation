package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Willi on 2/12/2019.
 */
public class MillstoneRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, MillstoneRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<Item, MillstoneRecipe> ITEM_MAP = new HashMap<>();
	private static final HashMap<String, MillstoneRecipe> OREDICT_MAP = new HashMap<>();
	private final ItemStack[] output;
	private final String name;
	private final Ingredient input;
	private final int time;
	private final float torque;

	private MillstoneRecipe(String name, Ingredient input, int time, float torque, ItemStack... output)
	{
		super(Collections.singletonList(input), null, Arrays.asList(output), null);
		this.name = name;
		this.input = input;
		this.time = time;
		this.torque = torque;
		this.output = output;
	}

	public static void AddRecipe(String name, String oreName, int time, float torque, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		MillstoneRecipe recipe = new MillstoneRecipe(name, Ingredient.fromTag(FATags.ForgeItemTag(oreName)), time, torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put("forge:" + oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, int time, float torque, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		MillstoneRecipe recipe = new MillstoneRecipe(name, Ingredient.fromStacks(new ItemStack(item, 1)), time,
				torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(item, recipe);
	}

	public static MillstoneRecipe FindRecipe(ItemStack input)
	{
		if (input.isEmpty())
			return null;
		if (ITEM_MAP.containsKey(input.getItem()))
			return ITEM_MAP.get(input.getItem());
		else
		{
			Set<ResourceLocation> oreIDs = input.getItem().getTags();
			for (ResourceLocation id : oreIDs)
			{
				if (OREDICT_MAP.containsKey(id.toString()))
					return OREDICT_MAP.get(id.toString());
			}
			return null;
		}
	}

	public static MillstoneRecipe GetRecipe(String name)
	{
		return STRING_MAP.getOrDefault(name, null);
	}

	public ItemStack[] GetOutputs()
	{
		return output;
	}

	public Ingredient GetInput()
	{
		return input;
	}

	public String GetName()
	{
		return name;
	}

	public int GetTime()
	{
		return time;
	}

	public float GetTorque()
	{
		return torque;
	}
}
