package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

/**
 * Created by Willi on 12/21/2017.
 */
public class FABaseBlock extends Block implements FABlock
{
	private String unlocalizedName;

	public FABaseBlock(Material blockMaterialIn, MapColor blockMapColorIn,
			String unlocalizedName)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.unlocalizedName = unlocalizedName;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		setResistance(10000);
		FABlocks.blocks.add(this);
		FAItems.items.add(new ItemBlock(this)
								  .setRegistryName(this.getRegistryName()));
	}

	public FABaseBlock(Material materialIn, String unlocalizedName)
	{
		this(materialIn, materialIn.getMaterialMapColor(), unlocalizedName);
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
}
