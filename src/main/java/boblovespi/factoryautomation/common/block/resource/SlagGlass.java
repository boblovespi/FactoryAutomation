package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

/**
 * Created by Willi on 12/23/2018.
 */
public class SlagGlass extends FABaseBlock
{
save)
	{
		super("slag_glass", false,
				Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(1.2f).harvestLevel(0)
						  .harvestTool(ToolType.PICKAXE), new Item.Properties().tab(FAItemGroups.resources));
		//		super(Material.GLASS, "slag_glass", FAItemGroups.resources);
		//		setLightOpacity(2);
		//		setSoundType(SoundType.GLASS);
		//		setHardness(1.2f);
		//		setHarvestLevel("pickaxe", 0);
	}

	//	@Override
	//	public BlockRenderLayer getRenderLayer()
	//	{
	//		return BlockRenderLayer.TRANSLUCENT;
	//	}

}
