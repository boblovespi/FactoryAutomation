package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Created by Willi on 8/26/2018.
 */
public class BronzeCauldron extends CauldronBlock implements FABlock
{
	public BronzeCauldron()
	{
		super(Properties.of(Material.METAL).strength(2.5f));
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// setResistance(10000);
		FABlocks.blocks.add(this);
		FAItemBlock item = new FAItemBlock(this, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
		FAItems.items.add(item);
	}

	@Override
	public String UnlocalizedName()
	{
		return "bronze_cauldron";
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "decoration/" + RegistryName();
	}
}
