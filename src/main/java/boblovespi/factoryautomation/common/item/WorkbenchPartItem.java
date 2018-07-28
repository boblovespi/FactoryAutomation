package boblovespi.factoryautomation.common.item;

import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Willi on 7/28/2018.
 */
public class WorkbenchPartItem extends FABaseItem
{
	public WorkbenchPartItem(String name, CreativeTabs creativeTab)
	{
		super(name, creativeTab);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "parts/" + super.GetMetaFilePath(meta);
	}
}
