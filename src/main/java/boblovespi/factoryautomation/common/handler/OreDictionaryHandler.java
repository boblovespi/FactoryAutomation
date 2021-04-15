package boblovespi.factoryautomation.common.handler;

import org.apache.commons.lang3.text.WordUtils;

@Deprecated
public class OreDictionaryHandler
{

	public static void registerOreDictionary()
	{
		//		// vanilla oredicts
		//		OreDictionary.registerOre("blockClay", Blocks.CLAY);
		//		OreDictionary.registerOre("slabCobblestone", new ItemStack(Blocks.STONE_SLAB, 1, 3));
		//
		//		// modded
		//		OreDictionary.registerOre("slag", FAItems.slag.ToItem());
		//		OreDictionary.registerOre("concrete", FABlocks.concrete.ToBlock());
		//		OreDictionary.registerOre("rice", FAItems.riceGrain.ToItem());
		//		OreDictionary.registerOre("wireCopper", FAItems.copperWire.ToItem());
		//		OreDictionary.registerOre("dustStone", FAItems.stoneDust.ToItem());
		//		OreDictionary.registerOre("dustAsh", FAItems.ash.ToItem());
		//		OreDictionary.registerOre("dustAcid", FAItems.acidPowder.ToItem());
		//		OreDictionary.registerOre("bowlGlycerin", FAItems.liquidGlycerin.ToItem());
		//		OreDictionary.registerOre("glycerin", FAItems.dryGlycerin.ToItem());
		//		OreDictionary.registerOre("ingotRubber", FAItems.rubber.ToItem());
		//		OreDictionary.registerOre("gemGraphite", FAItems.graphite.ToItem());
		//		OreDictionary.registerOre("tallow", FAItems.pigTallow.ToItem());
		//
		//		for (int i = 2; i < Metals.values().length; i++)
		//		{
		//			OreDictionary.registerOre(
		//					"ingot" + Cleanup(Metals.values()[i].getName()),
		//					new ItemStack(FAItems.ingot.GetItem(Metals.values()[i])));
		//			OreDictionary.registerOre(
		//					"nugget" + Cleanup(Metals.values()[i].getName()),
		//					new ItemStack(FAItems.nugget.GetItem(Metals.values()[i])));
		//			OreDictionary.registerOre(
		//					"block" + Cleanup(Metals.values()[i].getName()),
		//					new ItemStack(FABlocks.metalBlock.GetBlock(Metals.values()[i])));
		//		}
		//		for (int i = 0; i < Metals.values().length; i++)
		//		{
		//
		//			OreDictionary.registerOre(
		//					"plate" + Cleanup(Metals.values()[i].getName()),
		//					new ItemStack(FAItems.sheet.GetItem(Metals.values()[i])));
		//			OreDictionary.registerOre(
		//					"stick" + Cleanup(Metals.values()[i].getName()),
		//					new ItemStack(FAItems.rod.GetItem(Metals.values()[i])));
		//		}
		//		for (int i = 0; i < MetalOres.values().length; i++)
		//		{
		//			OreDictionary.registerOre(
		//					"ore" + Cleanup(MetalOres.values()[i].getName()),
		//					new ItemStack(FABlocks.metalOres.GetBlock(MetalOres.values()[i]).ToBlock()));
		//		}

	}

	public static String Cleanup(String name)
	{
		return WordUtils.capitalize(name, '_').replace("_", "");
	}
}