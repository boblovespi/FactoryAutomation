package boblovespi.factoryautomation.common.block.crafter.workbench;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 * Created by Willi on 4/15/2018.
 */
public abstract class Workbench extends FABaseBlock
{
	public Workbench(Material material, String name)
	{
		super(name, false, Properties.create(material).hardnessAndResistance(2),
				new Item.Properties().group(FAItemGroups.crafting));
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "workbench/" + RegistryName();
	}
}
