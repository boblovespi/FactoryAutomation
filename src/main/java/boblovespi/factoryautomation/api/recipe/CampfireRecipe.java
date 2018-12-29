package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.ItemInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Willi on 12/26/2018.
 */
public class CampfireRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, CampfireRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<ItemInfo, CampfireRecipe> ITEM_MAP = new HashMap<>();
	private static final HashMap<String, CampfireRecipe> OREDICT_MAP = new HashMap<>();
	private final ItemStack output;
	private final int time;
	private final String name;
	private final Ingredient input;

	private CampfireRecipe(String name, Ingredient input, ItemStack output, int time)
	{
		super(Collections.singletonList(input), null, Collections.singletonList(output), null);
		this.name = name;
		this.input = input;
		this.output = output;
		this.time = time;
	}

	public static void AddRecipe(String name, String oreName, ItemStack output, int time)
	{
		if (STRING_MAP.containsKey(name))
			return;
		CampfireRecipe recipe = new CampfireRecipe(name, new OreIngredient(oreName), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put(oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, int meta, ItemStack output, int time)
	{
		if (STRING_MAP.containsKey(name))
			return;
		CampfireRecipe recipe = new CampfireRecipe(
				name, Ingredient.fromStacks(new ItemStack(item, 1, meta)), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(new ItemInfo(item, meta), recipe);
	}

	public static CampfireRecipe FindRecipe(ItemStack input)
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

	public static CampfireRecipe GetRecipe(String name)
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

	public int GetTime()
	{
		return time;
	}
}
