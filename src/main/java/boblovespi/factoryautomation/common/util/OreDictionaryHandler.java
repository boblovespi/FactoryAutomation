package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.item.ItemStack;

public class OreDictionaryHandler {

    public static void registerOreDictionary() {

        net.minecraftforge.oredict.OreDictionary.registerOre("Slag", FAItems.slag.ToItem());
        net.minecraftforge.oredict.OreDictionary.registerOre("Concrete", FABlocks.concrete.ToBlock());
        net.minecraftforge.oredict.OreDictionary.registerOre("Rice", FAItems.riceGrain.ToItem());
        net.minecraftforge.oredict.OreDictionary.registerOre("ingotTin", new ItemStack(FAItems.ingot.ToItem(), 1, 3));
        net.minecraftforge.oredict.OreDictionary.registerOre("ingotCopper", new ItemStack(FAItems.ingot.ToItem(), 1, 2));
        net.minecraftforge.oredict.OreDictionary.registerOre("ingotBronze", new ItemStack(FAItems.ingot.ToItem(), 1, 4));
        net.minecraftforge.oredict.OreDictionary.registerOre("ingotSteel", new ItemStack(FAItems.ingot.ToItem(), 1, 5));
        net.minecraftforge.oredict.OreDictionary.registerOre("nuggetTin", new ItemStack(FAItems.nugget.ToItem(), 1, 3));
        net.minecraftforge.oredict.OreDictionary.registerOre("nuggetCopper", new ItemStack(FAItems.nugget.ToItem(), 1, 2));
        net.minecraftforge.oredict.OreDictionary.registerOre("nuggetBronze", new ItemStack(FAItems.nugget.ToItem(), 1, 4));
        net.minecraftforge.oredict.OreDictionary.registerOre("nuggetSteel", new ItemStack(FAItems.nugget.ToItem(), 1, 5));


    }
}
