package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

/**
 * Created by Willi on 12/23/2018.
 */
public class SlagGlass extends FABaseBlock {
	public SlagGlass() {
		super("slag_glass", false,
				Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(1.2f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.resources));
		//		setLightOpacity(2); // Todo: do something for the light opacity.
	}

	//	@Override
	//	public BlockRenderLayer getRenderLayer()
	//	{
	//		return BlockRenderLayer.TRANSLUCENT;
	//	}

}
