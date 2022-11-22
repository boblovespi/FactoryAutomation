package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 8/26/2018.
 */
public class BronzeFence extends FenceBlock
{
	public BronzeFence()
	{
		super(Properties.of(Material.METAL).strength(2.5f));
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(RegistryName());
		// setResistance(10000);
		FABlocks.blocks.add(RegistryObjectWrapper.Block("bronze_fence", this));
		FAItemBlock item = new FAItemBlock(this, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
		FAItems.items.add(RegistryObjectWrapper.Item("bronze_fence", item));
	}
}
