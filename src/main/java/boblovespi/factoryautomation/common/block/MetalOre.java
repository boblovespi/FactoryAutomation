package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.MetalOres;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Willi on 12/23/2017.
 */
public class MetalOre extends MultiTypeBlock<MetalOres>
{
	public MetalOre()
	{
		super(Material.ROCK, "ore_metal", MetalOres.class, "ores");
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(3);
		//		for (IBlockState state : blockState.getValidStates())
		//		{
		//			setHarvestLevel(
		//					"pickaxe", state.getValue(TYPE).harvestLevel, state);
		//		}
		TYPE = PropertyEnum.create("type", MetalOres.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (TYPE == null)
			TYPE = PropertyEnum.create("type", MetalOres.class);
		return super.createBlockState();
	}
}
