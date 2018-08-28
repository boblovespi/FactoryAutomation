package boblovespi.factoryautomation.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created by Willi on 3/7/2018.
 */
public class FAObjModelBlock extends FABaseBlock
{
	public FAObjModelBlock(Material blockMaterialIn, MapColor blockMapColorIn,
			String unlocalizedName)
	{
		super(blockMaterialIn, blockMapColorIn, unlocalizedName);
	}

	public FAObjModelBlock(Material materialIn, String unlocalizedName)
	{
		super(materialIn, unlocalizedName, null);

	}

	//	@Override
	//	public String RegistryName()
	//	{
	//		return "obj/" + UnlocalizedName();
	//	}
}
