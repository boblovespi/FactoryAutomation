package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.ItemInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.*;

/**
 * Created by Willi on 12/26/2018.
 */
public class ChoppingBlockRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, ChoppingBlockRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<ItemInfo, ChoppingBlockRecipe> ITEM_MAP = new HashMap<>();
	private static final HashMap<String, ChoppingBlockRecipe> OREDICT_MAP = new HashMap<>();
	private final ItemStack output;
	private final String name;
	private final Ingredient input;

	private ChoppingBlockRecipe(String name, Ingredient input, ItemStack output)
	{
		super(Collections.singletonList(input), null, Collections.singletonList(output), null);
		this.name = name;
		this.input = input;
		this.output = output;
	}

	public static void AddRecipe(String name, String oreName, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(name, new OreIngredient(oreName), output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put(oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, int meta, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
				name, Ingredient.fromStacks(new ItemStack(item, 1, meta)), output);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(new ItemInfo(item, meta), recipe);
	}

	public static ChoppingBlockRecipe FindRecipe(ItemStack input)
	{
		if (input.isEmpty())
			return null;

		ItemInfo itemInfo = new ItemInfo(input.getItem(), input.getMetadata());
		if (ITEM_MAP.containsKey(itemInfo))
			return ITEM_MAP.get(itemInfo);
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

	public static ChoppingBlockRecipe GetRecipe(String name)
	{
		return STRING_MAP.getOrDefault(name, null);
	}

	public ItemStack GetOutput()
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

	public static Collection<ChoppingBlockRecipe> GetRecipes()
	{
		return STRING_MAP.values();
	}
}
