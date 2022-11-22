package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.RegistryObjectWrapper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * Created by Willi on 12/21/2017.
 */
public class FABaseBlock extends Block
{
	protected FAItemBlock item;
	private String name;

	public FABaseBlock(String name, boolean hasCustomItem, Properties properties, Item.Properties itemProperties)
	{
		super(properties);
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		// setRegistryName(RegistryName());
		// setResistance(10000);
		// setCreativeTab(tab);

		FABlocks.blocks.add(RegistryObjectWrapper.Block(name, this));
		if (!hasCustomItem)
		{
			item = new FAItemBlock(this, itemProperties);
			FAItems.items.add(RegistryObjectWrapper.Item(name, this.item));
		}
	}

	public FABaseBlock(Material materialIn, String name, CreativeModeTab tab)
	{
		this(materialIn, name, tab, false);
	}

	public FABaseBlock(Material materialIn, String name, CreativeModeTab tab, boolean hasCustomItem)
	{
		this(name, hasCustomItem, Properties.of(materialIn), new Item.Properties().tab(tab));
	}

	public FABaseBlock(Material materialIn, MaterialColor blockMapColorIn, String name, CreativeModeTab tab)
	{
		this(name, false, Properties.of(materialIn, blockMapColorIn), new Item.Properties().tab(tab));
	}

	public FAItemBlock GetItem()
	{
		return item;
	}
}
