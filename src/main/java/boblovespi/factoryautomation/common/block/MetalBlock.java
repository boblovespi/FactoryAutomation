package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Created by Willi on 4/15/2018.
 */
public class MetalBlock extends MultiTypeBlock<Metals>
{
	public MetalBlock(String registryName, BlockBehaviour.Properties properties)
	{
		this(registryName, properties, false);
	}
	public MetalBlock(String registryName, BlockBehaviour.Properties properties, boolean ignoreVanilla)
	{
		super(registryName, Metals.class, "metals", properties.sound(SoundType.METAL),
				new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), ignoreVanilla ? 0b111 : 0);
	}
}
