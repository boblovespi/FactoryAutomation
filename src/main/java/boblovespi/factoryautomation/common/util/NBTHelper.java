package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Willi on 11/26/2017.
 */
public class NBTHelper
{
	public static MultiblockStructurePattern GetStructurePattern(NBTTagCompound compound, String key)
	{
		return MultiblockHandler.Get(compound.getString(key));
	}
}
