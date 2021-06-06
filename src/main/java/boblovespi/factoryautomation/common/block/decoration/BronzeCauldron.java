package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Created by Willi on 8/26/2018.
 */
public class BronzeCauldron extends CauldronBlock implements FABlock
{
	public BronzeCauldron()
	{
		super(Properties.of(Material.METAL).strength(2.5f));
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(registryName());
		// setResistance(10000);
		FABlocks.blocks.add(this);
		FAItemBlock item = new FAItemBlock(this, new Item.Properties().tab(ItemGroup.DECORATIONS));
		FAItems.items.add(item);
	}

	@Override
	public String unlocalizedName()
	{
		return "bronze_cauldron";
	}

	@Override
	public Block toBlock()
	{
		return this;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "decoration/" + registryName();
	}
}
