package boblovespi.factoryautomation.common.init;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHandler
{

	public static void registerOreDictionary()
	{

		OreDictionary.registerOre("slag", FAItems.slag.ToItem());
		OreDictionary.registerOre("concrete", FABlocks.concrete.ToBlock());
		OreDictionary.registerOre("rice", FAItems.riceGrain.ToItem());
		OreDictionary.registerOre("ingotTin",
				new ItemStack(FAItems.ingot.ToItem(), 1, 3));
		OreDictionary.registerOre("ingotCopper",
				new ItemStack(FAItems.ingot.ToItem(), 1, 2));
		OreDictionary.registerOre("ingotBronze",
				new ItemStack(FAItems.ingot.ToItem(), 1, 4));
		OreDictionary.registerOre("ingotSteel",
				new ItemStack(FAItems.ingot.ToItem(), 1, 5));
		OreDictionary.registerOre("nuggetTin",
				new ItemStack(FAItems.nugget.ToItem(), 1, 3));
		OreDictionary.registerOre("nuggetCopper",
				new ItemStack(FAItems.nugget.ToItem(), 1, 2));
		OreDictionary.registerOre("nuggetBronze",
				new ItemStack(FAItems.nugget.ToItem(), 1, 4));
		OreDictionary.registerOre("nuggetSteel",
				new ItemStack(FAItems.nugget.ToItem(), 1, 5));

	}
}
