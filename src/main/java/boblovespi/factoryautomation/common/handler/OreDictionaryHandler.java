package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

public class OreDictionaryHandler
{

	public static void registerOreDictionary()
	{

		OreDictionary.registerOre("slag", FAItems.slag.ToItem());
		OreDictionary.registerOre("concrete", FABlocks.concrete.ToBlock());
		OreDictionary.registerOre("rice", FAItems.riceGrain.ToItem());

		for (int i = 2; i < Metals.values().length; i++)
		{
			OreDictionary.registerOre(
					"ingot" + Cleanup(Metals.values()[i].getName()),
					new ItemStack(FAItems.ingot.GetItem(Metals.values()[i])));
			OreDictionary.registerOre(
					"nugget" + Cleanup(Metals.values()[i].getName()),
					new ItemStack(FAItems.nugget.GetItem(Metals.values()[i])));
			OreDictionary.registerOre(
					"block" + Cleanup(Metals.values()[i].getName()),
					new ItemStack(FABlocks.metalBlock.GetBlock(Metals.values()[i])));
		}
		for (int i = 0; i < Metals.values().length; i++)
		{

			OreDictionary.registerOre(
					"plate" + Cleanup(Metals.values()[i].getName()),
					new ItemStack(FAItems.sheet.GetItem(Metals.values()[i])));
		}
		for (int i = 0; i < MetalOres.values().length; i++)
		{
			OreDictionary.registerOre(
					"ore" + Cleanup(MetalOres.values()[i].getName()),
					new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.values()[i]).ToBlock()));
		}

	}

	private static String Cleanup(String name)
	{
		return WordUtils.capitalize(name, '_').replace("_", "");
	}
}