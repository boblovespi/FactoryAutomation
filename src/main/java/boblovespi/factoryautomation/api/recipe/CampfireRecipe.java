package boblovespi.factoryautomation.api.recipe;

import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Willi on 12/26/2018.
 */
public class CampfireRecipe extends ChancelessMachineRecipe
{
	private static final HashMap<String, CampfireRecipe> STRING_MAP = new HashMap<>();
	private static final HashMap<Item, CampfireRecipe> ITEM_MAP = new HashMap<>();
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
		CampfireRecipe recipe = new CampfireRecipe(
				name, Ingredient.fromTag(FATags.ForgeItemTag(oreName)), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		OREDICT_MAP.put("forge:" + oreName, recipe);
	}

	public static void AddRecipe(String name, Item item, ItemStack output, int time)
	{
		if (STRING_MAP.containsKey(name))
			return;
		CampfireRecipe recipe = new CampfireRecipe(name, Ingredient.fromStacks(new ItemStack(item, 1)), output, time);
		STRING_MAP.putIfAbsent(name, recipe);
		ITEM_MAP.put(item, recipe);
	}

	public static CampfireRecipe FindRecipe(ItemStack input)
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
