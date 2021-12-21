package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.block.Materials;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;

/**
 * Created by Willi on 1/28/2019.
 */
public class IronBloom extends FABaseBlock
{
	public IronBloom()
	{
		super("iron_bloom", false, Properties.of(Materials.BLOOM).requiresCorrectToolForDrops().strength(4).requiresCorrectToolForDrops(),
				new Item.Properties().tab(FAItemGroups.metallurgy));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}
	// TODO: loot tables
	//	/**
	//	 * This gets a complete list of items dropped from this block.
	//	 */
	//	@Override
	//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess level, BlockPos pos, BlockState state, int fortune)
	//	{
	//		drops.add(new ItemStack(FAItems.slag.ToItem(), 1 + Randoms.MAIN.r.nextInt(2 + fortune / 2)));
	//		drops.add(new ItemStack(FAItems.ironShard.ToItem(), 2 + Randoms.MAIN.r.nextInt(1 + fortune)));
	//	}
}
