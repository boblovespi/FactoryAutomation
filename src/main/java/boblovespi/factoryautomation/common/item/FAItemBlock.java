package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

/**
 * Created by Willi on 11/9/2017.
 * usable again!
 */
public class FAItemBlock extends ItemBlock implements FAItem
{

	public final FABlock faBlock;
	private final Block block;

	public FAItemBlock(FABlock base)
	{
		super(base.ToBlock());
		this.block = base.ToBlock();
		this.faBlock = base;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// FAItems.items.add(this);
	}

	@Override
	public String UnlocalizedName()
	{
		return faBlock.UnlocalizedName();
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return faBlock.GetMetaFilePath(meta);
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
