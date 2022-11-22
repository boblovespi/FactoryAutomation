package boblovespi.factoryautomation.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * Created by Willi on 11/9/2017.
 * usable again!
 */
public class FAItemBlock extends BlockItem
{
	public final Block faBlock;
	private final Block block;

	public FAItemBlock(Block base, Properties properties)
	{
		super(base, properties);
		this.block = base;
		this.faBlock = base;
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(RegistryName());
		// FAItems.items.add(this);
	}
}
