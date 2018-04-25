package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.MultiStateBlock;
import boblovespi.factoryautomation.common.item.types.IMultiTypeEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 12/23/2017.
 */
public class MultiTypeItemBlock<T extends Enum<T> & IMultiTypeEnum & IStringSerializable> extends ItemBlock
		implements FAItem
{

	public Class<T> blockTypes;
	private MultiStateBlock baseBlock;

	public MultiTypeItemBlock(MultiStateBlock block, Class<T> blockTypes)
	{
		super(block);
		baseBlock = block;
		this.blockTypes = blockTypes;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName() == null ? UnlocalizedName() : RegistryName());
		setHasSubtypes(true);
	}

	public void SetBaseBlock(MultiStateBlock baseBlock)
	{
		this.baseBlock = baseBlock;
	}

	@Override
	public String UnlocalizedName()
	{
		return baseBlock.UnlocalizedName();
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return baseBlock.GetMetaFilePath(meta);
	}

	@Override
	public Item ToItem()
	{
		return this;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		T[] types = blockTypes.getEnumConstants();

		for (int i = 0; i < types.length; i++)
			if (stack.getItemDamage() == i)
				return getUnlocalizedName() + "." + types[i].getName();

		return getUnlocalizedName() + "." + types[0].getName();
	}


	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
	 * placed as a Block (mostly used with ItemBlocks).
	 */
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
