package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

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
					"ingot" + StringUtils
							.capitalize(Metals.values()[i].getName()),
					new ItemStack(FAItems.ingot.ToItem(), 1, i));
			OreDictionary.registerOre(
					"nugget" + StringUtils
							.capitalize(Metals.values()[i].getName()),
					new ItemStack(FAItems.nugget.ToItem(), 1, i));
		}
		for (int i = 0; i < Metals.values().length; i++)
		{

			OreDictionary.registerOre(
					"plate" + StringUtils
							.capitalize(Metals.values()[i].getName()),
					new ItemStack(FAItems.sheet.ToItem(), 1, i));
		}
		for (int i = 0; i < MetalOres.values().length; i++)
		{
			OreDictionary.registerOre(
					"ore" + StringUtils
							.capitalize(MetalOres.values()[i].getName()),
					new ItemStack(FAItems.metalOre, 1, i));
		}

	}
}