package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Willi on 4/15/2018.
 */
public class WorkbenchToolItem extends ToolItem implements FAItem
{
	private final String name;

	public WorkbenchToolItem(String name, float damage, float speed, IItemTier material, Set<Block> effectiveBlocks,
			Properties properties, ToolType toolType)
	{
		super(damage, speed, material, effectiveBlocks,
				properties.addToolType(toolType, material.getLevel()).tab(FAItemGroups.tools));
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(registryName() == null ? unlocalizedName() : registryName());
		// setCreativeTab(FAItemGroups.tools);
		FAItems.items.add(this);
	}

	public WorkbenchToolItem(String name, float damage, float speed, IItemTier material, Properties properties,
			ToolType toolType)
	{
		this(name, damage, speed, material, new HashSet<>(), properties, toolType);
	}

	@Override
	public String unlocalizedName()
	{
		return name;
	}

	@Override
	public String getMetaFilePath(int meta)
	{
		return "tools/" + unlocalizedName();
	}

	@Override
	public Item toItem()
	{
		return this;
	}
}
