package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Material;

/**
 * Created by Willi on 1/28/2019.
 */
public class CharcoalPile extends FABaseBlock
{
	public CharcoalPile()
	{
		super("charcoal_pile", false, Properties.of(Material.DIRT).strength(0.5f).requiresCorrectToolForDrops(),
				new Item.Properties().tab(FAItemGroups.resources));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	// TODO: add loot tables
	//	/**
	//	 * Get the Item that this Block should drop when harvested.
	//	 */
	//	@Override
	//	public Item getItemDropped(BlockState state, Random rand, int fortune)
	//	{
	//		return Items.COAL;
	//	}
	//
	//	/**
	//	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	//	 * returns the metadata of the dropped item based on the old metadata of the block.
	//	 */
	//	@Override
	//	public int damageDropped(BlockState state)
	//	{
	//		return 1;
	//	}
	//
	//	/**
	//	 * State and fortune sensitive version, this replaces the old (int meta, Random rand)
	//	 * version in 1.1.
	//	 */
	//	@Override
	//	public int quantityDropped(BlockState state, int fortune, Random random)
	//	{
	//		return 5 + random.nextInt(4 + fortune);
	//	}
}
