package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Created by Willi on 4/15/2018.
 */
public class MetalBlock extends MultiTypeBlock<Metals>
{
	public MetalBlock(String registeryName, Properties properties)
	{
		super(registeryName, Metals.class, "metals", properties.sound(SoundType.METAL),
				new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		if (meta > 1 && meta < blockTypes.getEnumConstants().length)
			return super.GetMetaFilePath(meta);
		else
			return super.GetMetaFilePath(2);
	}
}
