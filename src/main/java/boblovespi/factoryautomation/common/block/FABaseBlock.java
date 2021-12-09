package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItemBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * Created by Willi on 12/21/2017.
 */
public class FABaseBlock extends Block implements FABlock
{
	protected FAItemBlock item;
	private String unlocalizedName;

	public FABaseBlock(String name, boolean hasCustomItem, Properties properties, Item.Properties itemProperties)
	{
		super(properties);
		this.unlocalizedName = name;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName());
		// setResistance(10000);
		// setCreativeTab(tab);

		FABlocks.blocks.add(this);
		if (!hasCustomItem)
		{
			item = new FAItemBlock(this, itemProperties);
			FAItems.items.add(this.item);
		}
	}

	public FABaseBlock(Material materialIn, String unlocalizedName, CreativeModeTab tab)
	{
		this(materialIn, unlocalizedName, tab, false);
	}

	public FABaseBlock(Material materialIn, String unlocalizedName, CreativeModeTab tab, boolean hasCustomItem)
	{
		this(unlocalizedName, hasCustomItem, Properties.of(materialIn), new Item.Properties().tab(tab));
	}

	public FABaseBlock(Material materialIn, MaterialColor blockMapColorIn, String unlocalizedName, CreativeModeTab tab)
	{
		this(unlocalizedName, false, Properties.of(materialIn, blockMapColorIn), new Item.Properties().tab(tab));
	}

	@Override
	public String UnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public Block ToBlock()
	{
		return this;
	}

	public FAItem GetItem()
	{
		return item;
	}
}
