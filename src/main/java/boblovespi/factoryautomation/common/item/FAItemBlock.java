package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.minecraft.world.item.Item.Properties;

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
		super(base.ToBlock(), properties);
		this.block = base.ToBlock();
		this.faBlock = base;
		// setUnlocalizedName(UnlocalizedName());
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
