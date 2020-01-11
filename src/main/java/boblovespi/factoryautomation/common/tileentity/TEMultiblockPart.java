package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Willi on 11/26/2017.
 */
public class TEMultiblockPart extends TileEntity
{
	private int[] structurePosition = new int[3]; // the position of the block in the structure measured by {+x, +y, +z}
	private String structureId = null; // the id of the multiblock structure
	private int[] structureOffset = new int[3]; // the offset from the controller, in world coordinates
	private BlockState state;
	private IMultiblockControllerTE controller;

	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad()
	{
		if (!world.isRemote && structureId != null)
			controller = (IMultiblockControllerTE) world
					.getTileEntity(pos.add(-structureOffset[0], -structureOffset[1], -structureOffset[2]));
	}

	public void SetMultiblockInformation(String structure, int posX, int posY, int posZ, int offX, int offY, int offZ,
			BlockState blockState)
	{
		structureId = structure;
		structurePosition[0] = posX;
		structurePosition[1] = posY;
		structurePosition[2] = posZ;
		structureOffset[0] = offX;
		structureOffset[1] = offY;
		structureOffset[2] = offZ;
		state = blockState;
		controller = (IMultiblockControllerTE) world.getTileEntity(pos.add(-offX, -offY, -offZ));
	}

	public void SetMultiblockInformation(String strucuture, BlockPos pos, BlockPos offset, BlockState blockState)
	{
		SetMultiblockInformation(strucuture, pos.getX(), pos.getY(), pos.getZ(), offset.getX(), offset.getY(),
				offset.getZ(), blockState);
	}

	@Override
	public void readFromNBT(CompoundNBT compound)
	{
		super.readFromNBT(compound);
		structurePosition = compound.getIntArray("structurePosition");
		structureId = compound.getString("structure");
		structureOffset = compound.getIntArray("structureOffset");
		state = NBTHelper.GetBlockState(compound, "blockState");
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound)
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

	public BlockState GetBlockState()
	{
		return state;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (controller == null)
			return null;
		return controller.GetCapability(capability, structurePosition, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		if (controller == null)
			return false;
		return controller.GetCapability(capability, structurePosition, facing) != null;
	}
}
