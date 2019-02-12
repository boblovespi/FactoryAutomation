package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import scala.actors.threadpool.Arrays;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Willi on 2/12/2019.
 */
public class MillstoneRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, MillstoneRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<String, MillstoneRecipe> ITEM_MAP = new HashMap<>();
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
		MillstoneRecipe recipe = new MillstoneRecipe(name, new OreIngredient(oreName), time, torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put(oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, int meta, int time, float torque, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		MillstoneRecipe recipe = new MillstoneRecipe(name, Ingredient.fromStacks(new ItemStack(item, 1, meta)), time,
				torque, output);
		STRING_MAP.putIfAbsent(name, recipe);
		String key = ItemHelper.GetItemID(item, meta);
		ITEM_MAP.put(key, recipe);
	}

	public static MillstoneRecipe FindRecipe(ItemStack input)
	{
		if (input.isEmpty())
			return null;
		String key = ItemHelper.GetItemID(input);
		if (ITEM_MAP.containsKey(key))
			return ITEM_MAP.get(key);
		else
		{
			int[] oreIDs = OreDictionary.getOreIDs(input);
			for (int id : oreIDs)
			{
				if (OREDICT_MAP.containsKey(OreDictionary.getOreName(id)))
					return OREDICT_MAP.get(OreDictionary.getOreName(id));
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
