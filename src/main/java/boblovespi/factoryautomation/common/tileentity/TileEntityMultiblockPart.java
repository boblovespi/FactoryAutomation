package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Willi on 11/26/2017.
 */
public class TileEntityMultiblockPart extends TileEntity
{
	private int[] structurePosition = new int[3]; // the position of the block in the structure measured by {+x, +y, +z}
	private String structureId; // the id of the multiblock structure
	private int[] structureOffset = new int[3]; // the offset from the controller, in world coordinates
	private IBlockState state;

	public void SetMultiblockInformation(String structure, int posX, int posY,
			int posZ, int offX, int offY, int offZ, IBlockState blockState)
	{
		structureId = structure;
		structurePosition[0] = posX;
		structurePosition[1] = posY;
		structurePosition[2] = posZ;
		structureOffset[0] = offX;
		structureOffset[1] = offY;
		structureOffset[2] = offZ;
		state = blockState;
	}

	public void SetMultiblockInformation(String strucuture, BlockPos pos,
			BlockPos offset, IBlockState blockState)
	{
		SetMultiblockInformation(
				strucuture, pos.getX(), pos.getY(), pos.getZ(), offset.getX(),
				offset.getY(), offset.getZ(), blockState);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		structurePosition = compound.getIntArray("structurePosition");
		structureId = compound.getString("structure");
		structureOffset = compound.getIntArray("structureOffset");
		state = NBTHelper.GetBlockState(compound, "blockState");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setIntArray("structurePosition", structurePosition);
		compound.setString("structure", structureId);
		compound.setIntArray("structureOffset", structureOffset);
		NBTHelper.SetBlockState(compound, state, "blockState");
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

	public IBlockState GetBlockState()
	{
		return state;
	}
}
