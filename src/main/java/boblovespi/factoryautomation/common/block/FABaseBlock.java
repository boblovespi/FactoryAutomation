package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Willi on 12/21/2017.
 */
public class FABaseBlock extends Block implements FABlock
{
	private String unlocalizedName;
	private FAItemBlock item;

	public FABaseBlock(Material blockMaterialIn, MapColor blockMapColorIn, String unlocalizedName)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.unlocalizedName = unlocalizedName;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setResistance(10000);
		FABlocks.blocks.add(this);
		item = new FAItemBlock(this);
		FAItems.items.add(item);
	}

	public FABaseBlock(Material materialIn, String unlocalizedName, CreativeTabs tab)
	{
		this(materialIn, materialIn.getMaterialMapColor(), unlocalizedName);
		this.setCreativeTab(tab);
	}

	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	public FAItem GetItem()
	{
		return item;
	}
}
