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
	protected FAItemBlock item;
	private String unlocalizedName;

	public FABaseBlock(Material blockMaterialIn, MapColor blockMapColorIn, String unlocalizedName, CreativeTabs tab,
			boolean hasCustomItem)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.unlocalizedName = unlocalizedName;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setResistance(10000);
		setCreativeTab(tab);

		FABlocks.blocks.add(this);
		if (!hasCustomItem)
		{
			item = new FAItemBlock(this);
			FAItems.items.add(this.item);
		}
	}

	public FABaseBlock(Material materialIn, String unlocalizedName, CreativeTabs tab)
	{
		this(materialIn, materialIn.getMaterialMapColor(), unlocalizedName, tab);
	}

	public FABaseBlock(Material materialIn, String unlocalizedName, CreativeTabs tab, boolean hasCustomItem)
	{
		this(materialIn, materialIn.getMaterialMapColor(), unlocalizedName, tab, hasCustomItem);
	}

	public FABaseBlock(Material materialIn, MapColor blockMapColorIn, String unlocalizedName, CreativeTabs tab)
	{
		this(materialIn, blockMapColorIn, unlocalizedName, tab, false);
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
