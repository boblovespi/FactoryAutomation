package boblovespi.factoryautomation.common.block;

import net.minecraft.block.Block;

/**
 * Created by Willi on 4/12/2017.
 */
public interface FABlock
{
	String UnlocalizedName();

	default String RegistryName()
	{
		return UnlocalizedName();
	}

	default String GetMetaFilePath(int meta)
	{
		return UnlocalizedName();
	}

	Block ToBlock();

	default boolean IsItemBlock()
	{
		return true;
	}
}
