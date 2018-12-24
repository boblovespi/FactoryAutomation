package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

/**
 * Created by Willi on 12/23/2018.
 */
public class SlagGlass extends FABaseBlock
{
	public SlagGlass()
	{
		super(Material.GLASS, "slag_glass", FACreativeTabs.resources);
		setLightOpacity(2);
		setSoundType(SoundType.GLASS);
		setHardness(1.2f);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
}
