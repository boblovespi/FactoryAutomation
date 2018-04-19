package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.MultiTypeItemBlock;
import boblovespi.factoryautomation.common.item.types.Metals;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by Willi on 4/15/2018.
 */
public class MetalBlock extends MultiTypeBlock<Metals>
{
	private MultiTypeItemBlock<Metals> itemBlock = new MultiTypeItemBlock<Metals>(this, Metals.class)
	{
		{
			setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		}

		@Override
		public String getUnlocalizedName(ItemStack stack)
		{
			int meta = stack.getItemDamage();
			if (!(meta > 1) && (meta < blockTypes.getEnumConstants().length))
			{
				ItemStack clone = new ItemStack(stack.getItem(), stack.getCount(), 2);
				return super.getUnlocalizedName(clone);
			}
			return super.getUnlocalizedName(stack);
		}

		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
		{
			if (!isInCreativeTab(tab))
				return;
			for (int i = 2; i < blockTypes.getEnumConstants().length; i++)
			{
				items.add(new ItemStack(this, 1, i));
			}
		}
	};

	public MetalBlock()
	{
		super(Material.IRON, Material.IRON.getMaterialMapColor(), "metal_block", Metals.class, "metals",
				CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		if (meta > 1 && meta < blockTypes.getEnumConstants().length)
			return super.GetMetaFilePath(meta);
		else
			return super.GetMetaFilePath(2);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		if (TYPE == null)
			TYPE = PropertyEnum.create("type", Metals.class);
		return super.createBlockState();
	}
}
