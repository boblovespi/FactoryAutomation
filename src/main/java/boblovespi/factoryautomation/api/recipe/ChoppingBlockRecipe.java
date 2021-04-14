package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Willi on 12/26/2018.
 */
public class ChoppingBlockRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, ChoppingBlockRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<Item, ChoppingBlockRecipe> ITEM_MAP = new HashMap<>();
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
		ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
				name, Ingredient.fromTag(FATags.ForgeItemTag(oreName)), output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put("forge:" + oreName, recipe);
	}

	public static void AddRecipe(String name, ResourceLocation oreName, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
				name, Ingredient.fromTag(ItemTags.createOptional(oreName)), output);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put(oreName.toString(), recipe);
	}

	public static void AddRecipe(String name, Item item, ItemStack output)
	{
		if (STRING_MAP.containsKey(name))
			return;
		ChoppingBlockRecipe recipe = new ChoppingBlockRecipe(
				name, Ingredient.fromStacks(new ItemStack(item, 1)), output);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(item, recipe);
	}

	public static ChoppingBlockRecipe FindRecipe(ItemStack input)
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
