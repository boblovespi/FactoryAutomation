package boblovespi.factoryautomation.common.item;

import net.minecraft.item.ItemGroup;

/**
 * Created by Willi on 7/28/2018.
 */
public class WorkbenchPartItem extends FABaseItem
{
	public WorkbenchPartItem(String name, ItemGroup creativeTab)
	{
		super(name, creativeTab);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "parts/" + super.GetMetaFilePath(meta);
	}
}
