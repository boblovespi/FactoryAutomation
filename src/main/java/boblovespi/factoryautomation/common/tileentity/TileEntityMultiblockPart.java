package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Willi on 11/26/2017.
 */
public class TileEntityMultiblockPart extends TileEntity
{
	private int[] structureControllerOffset = new int[3]; // offset measured by {+x, +y, +z}
	private int blockStateIdToDrop; // the id of the block that the block should drop

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		structureControllerOffset = compound.getIntArray("offset");
		blockStateIdToDrop = compound.getInteger("strucutre");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setIntArray("offset", structureControllerOffset);
		compound.setInteger("structure", blockStateIdToDrop);
		return super.writeToNBT(compound);
	}

	public int getBlockStateIdToDrop()
	{
		return blockStateIdToDrop;
	}

	public int[] getStructureControllerOffset()
	{
		return structureControllerOffset;
	}
}
