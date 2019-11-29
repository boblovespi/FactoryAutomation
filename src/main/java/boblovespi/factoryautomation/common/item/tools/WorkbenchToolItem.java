package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.item.FAItem;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ToolItem;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Willi on 4/15/2018.
 */
public class WorkbenchToolItem extends ToolItem implements FAItem
{
	private final String name;

	public WorkbenchToolItem(String name, float attackDamageIn, float attackSpeedIn, IItemTier materialIn,
			Set<Block> effectiveBlocksIn)
	{
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		this.name = name;
		setUnlocalizedName(UnlocalizedName());
		setRegistryName(
				RegistryName() == null ? UnlocalizedName() : RegistryName());
		setCreativeTab(FAItemGroups.tools);
		FAItems.items.add(this);
	}

	public WorkbenchToolItem(String name, float attackDamageIn, float attackSpeedIn, IItemTier materialIn)
	{
		this(name, attackDamageIn, attackSpeedIn, materialIn, new HashSet<>());
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
