package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Willi on 11/26/2017.
 */
public class TileEntityMultiblockPart extends TileEntity
{
	private int[] structurePosition = new int[3]; // the position of the block in the structure measured by {+x, +y, +z}
	private String structureId; // the id of the multiblock structure
	private int[] structureOffset = new int[3]; // the offset from the controller, in world coordinates

	public void SetMultiblockInformation(String structure, int posX, int posY,
			int posZ, int offX, int offY, int offZ)
	{
		structureId = structure;
		structurePosition[0] = posX;
		structurePosition[1] = posY;
		structurePosition[2] = posZ;
		structureOffset[0] = offX;
		structureOffset[1] = offY;
		structureOffset[2] = offZ;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		structurePosition = compound.getIntArray("structurePosition");
		structureId = compound.getString("structure");
		structureOffset = compound.getIntArray("structureOffset");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setIntArray("structurePosition", structurePosition);
		compound.setString("structure", structureId);
		compound.setIntArray("structureOffset", structureOffset);
		return super.writeToNBT(compound);
	}

	public String GetStructureId()
	{
		return structureId;
	}

	public int[] GetPosition()
	{
		return structurePosition;
	}

	public int[] GetOffset()
	{
		return structureOffset;
	}
}
