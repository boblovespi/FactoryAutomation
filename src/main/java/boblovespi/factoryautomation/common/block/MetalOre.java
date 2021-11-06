package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Created by Willi on 12/23/2017.
 */
public class MetalOre extends MultiTypeBlock<MetalOres>
{
	public MetalOre()
	{
		super("ore_metal", MetalOres.class, "ores", AbstractBlock.Properties.of(Material.STONE).strength(3, 2.7f),
				new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS));
	}
}
