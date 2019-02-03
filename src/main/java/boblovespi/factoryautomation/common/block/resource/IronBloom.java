package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import boblovespi.factoryautomation.common.util.Randoms;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Willi on 1/28/2019.
 */
public class IronBloom extends FABaseBlock
{
	public IronBloom()
	{
		super(Material.IRON, "iron_bloom", FACreativeTabs.metallurgy);
		setHardness(4.0f);
		setHarvestLevel("hammer", 0);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	/**
	 * This gets a complete list of items dropped from this block.
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(new ItemStack(FAItems.slag.ToItem(), 1 + Randoms.MAIN.r.nextInt(2 + fortune / 2)));
		drops.add(new ItemStack(FAItems.ironShard.ToItem(), 2 + Randoms.MAIN.r.nextInt(1 + fortune)));
	}
}
