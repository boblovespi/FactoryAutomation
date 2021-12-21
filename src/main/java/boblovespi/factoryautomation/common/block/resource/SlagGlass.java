package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 12/23/2018.
 */
public class SlagGlass extends FABaseBlock
{
	public SlagGlass()
	{
		super("slag_glass", false,
				Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(1.2f).requiresCorrectToolForDrops(), new Item.Properties().tab(FAItemGroups.resources));
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
