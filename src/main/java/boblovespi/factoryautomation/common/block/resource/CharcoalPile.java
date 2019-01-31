package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FACreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Created by Willi on 1/28/2019.
 */
public class CharcoalPile extends FABaseBlock
{
	public CharcoalPile()
	{
		super(Material.GROUND, "charcoal_pile", FACreativeTabs.resources);
		setHardness(1);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "resources/" + RegistryName();
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.COAL;
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return 1;
	}

	/**
	 * State and fortune sensitive version, this replaces the old (int meta, Random rand)
	 * version in 1.1.
	 */
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 2 + random.nextInt(2 + fortune);
	}
}
