package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 8/26/2018.
 */
public class BronzeCauldron extends CauldronBlock
{
	public BronzeCauldron()
	{
		super(Properties.of(Material.METAL).strength(2.5f));
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(RegistryName());
		// setResistance(10000);
		FABlocks.blocks.add(RegistryObjectWrapper.Block("bronze_cauldron", this));
		FAItemBlock item = new FAItemBlock(this, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
		FAItems.items.add(RegistryObjectWrapper.Item("bronze_cauldron", item));
	}
}
