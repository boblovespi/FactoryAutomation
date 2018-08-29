package boblovespi.factoryautomation.common.block.workbench;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import net.minecraft.block.material.Material;

/**
 * Created by Willi on 4/15/2018.
 */
public abstract class Workbench extends FABaseBlock
{
	public Workbench(Material blockMaterialIn, String unlocalizedName)
	{
		super(blockMaterialIn, unlocalizedName, null);
	}

	@Override
	public String GetMetaFilePath(int meta)
	{
		return "workbench/" + RegistryName();
	}
}
