package boblovespi.factoryautomation.common.block.decoration;

import boblovespi.factoryautomation.common.block.FABlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;

/**
 * Created by Willi on 8/26/2018.
 */
public class BronzeCauldron extends BlockCauldron implements FABlock
{
	public BronzeCauldron()
	{
		super();
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setResistance(10000);
		FABlocks.blocks.add(this);
		FAItemBlock item = new FAItemBlock(this);
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
