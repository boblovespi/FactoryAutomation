package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Willi on 4/15/2018.
 */
public class MetalBlock extends MultiTypeBlock<Metals>
{
	public MetalBlock()
	{
		super(Material.IRON, Material.IRON.getMaterialMapColor(), "metal_block", Metals.class, "metals",
				CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		if (meta > 1 && meta < blockTypes.getEnumConstants().length)
			return super.GetMetaFilePath(meta);
		else
			return super.GetMetaFilePath(2);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (TYPE == null)
			TYPE = PropertyEnum.create("type", Metals.class);
		return super.createBlockState();
	}
}
