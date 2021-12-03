package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

/**
 * Created by Willi on 12/23/2017.
 */
public class MetalOre extends MultiTypeBlock<MetalOres>
{
	public MetalOre()
	{
		super("ore_metal", MetalOres.class, "ores", BlockBehaviour.Properties.of(Material.STONE).strength(3, 2.7f),
				new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
	}
}
