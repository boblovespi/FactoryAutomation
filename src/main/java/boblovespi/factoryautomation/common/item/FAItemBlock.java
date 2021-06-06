package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * Created by Willi on 11/9/2017.
 * usable again!
 */
public class FAItemBlock extends BlockItem implements FAItem
{

	public final FABlock faBlock;
	private final Block block;

	public FAItemBlock(FABlock base, Properties properties)
	{
		super(base.toBlock(), properties);
		this.block = base.toBlock();
		this.faBlock = base;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(registryName());
		// FAItems.items.add(this);
	}

	@Override
	public String unlocalizedName()
	{
		return faBlock.unlocalizedName();
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return faBlock.getMetaFilePath(meta);
	}

	@Override
	public Item toItem()
	{
		return this;
	}
}
