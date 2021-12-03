package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DiggerItem;
import net.minecraftforge.common.ToolType;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.item.Item.Properties;

/**
 * Created by Willi on 4/15/2018.
 */
public class WorkbenchToolItem extends DiggerItem implements FAItem
{
	private final String name;

	public WorkbenchToolItem(String name, float damage, float speed, Tier material, Set<Block> effectiveBlocks,
			Properties properties, ToolType toolType)
	{
		super(damage, speed, material, effectiveBlocks,
				properties.addToolType(toolType, material.getLevel()).tab(FAItemGroups.tools));
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName() == null ? UnlocalizedName() : RegistryName());
		// setCreativeTab(FAItemGroups.tools);
		FAItems.items.add(this);
	}

	public WorkbenchToolItem(String name, float damage, float speed, Tier material, Properties properties,
			ToolType toolType)
	{
		this(name, damage, speed, material, new HashSet<>(), properties, toolType);
	}

	@Override
	public String UnlocalizedName()
	{
		return name;
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "tools/" + UnlocalizedName();
	}

	@Override
	public Item ToItem()
	{
		return this;
	}
}
