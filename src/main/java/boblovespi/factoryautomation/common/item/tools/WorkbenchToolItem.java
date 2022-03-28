package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;

/**
 * Created by Willi on 4/15/2018.
 */
public class WorkbenchToolItem extends DiggerItem implements FAItem
{
	private final String name;

	public WorkbenchToolItem(String name, float damage, float speed, Tier material, Tag<Block> toolType,
							 Properties properties)
	{
		super(damage, speed, material, toolType, properties.tab(FAItemGroups.tools));
		this.name = name;
		// setUnlocalizedName(UnlocalizedName());
		setRegistryName(RegistryName() == null ? UnlocalizedName() : RegistryName());
		// setCreativeTab(FAItemGroups.tools);
		FAItems.items.add(this);
		if (material == Tiers.IRON)
			maxDamage = 320;
	}

	public WorkbenchToolItem(String name, float damage, float speed, Tier material, Properties properties,
							 Tag<Block> toolType)
	{
		this(name, damage, speed, material, toolType, properties);
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
