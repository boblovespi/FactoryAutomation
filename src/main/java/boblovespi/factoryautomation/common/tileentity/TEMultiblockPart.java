package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

import static boblovespi.factoryautomation.common.tileentity.TileEntityHandler.teMultiblockPart;

/**
 * Created by Willi on 11/26/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEMultiblockPart extends BlockEntity
{
	private int[] structurePosition = new int[3]; // the position of the block in the structure measured by {+x, +y, +z}
	private String structureId = null; // the id of the multiblock structure
	private int[] structureOffset = new int[3]; // the offset from the controller, in level coordinates
	private BlockState state;
	private IMultiblockControllerTE controller;

	public TEMultiblockPart(BlockPos pos, BlockState state)
	{
		super(teMultiblockPart, pos, state);
	}

	/**
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	public void InitController()
	{
		if (controller == null && !Objects.requireNonNull(level).isClientSide && structureId != null)
			controller = (IMultiblockControllerTE) level
					.getBlockEntity(worldPosition.offset(-structureOffset[0], -structureOffset[1], -structureOffset[2]));
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
		controller = (IMultiblockControllerTE) Objects.requireNonNull(level).getBlockEntity(worldPosition.offset(-offX, -offY, -offZ));
	}

	public void SetMultiblockInformation(String structure, BlockPos worldPosition, BlockPos offset, BlockState blockState)
	{
		SetMultiblockInformation(structure, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), offset.getX(), offset.getY(),
				offset.getZ(), blockState);
		setChanged();
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		structurePosition = compound.getIntArray("structurePosition");
		structureId = compound.getString("structure");
		structureOffset = compound.getIntArray("structureOffset");
		this.state = NBTHelper.GetBlockState(compound, "blockState");
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.putIntArray("structurePosition", structurePosition);
		compound.putString("structure", structureId);
		compound.putIntArray("structureOffset", structureOffset);
		NBTHelper.SetBlockState(compound, state, "blockState");
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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (controller == null)
		{
			InitController();
			return LazyOptional.empty();
		}
		return controller.GetCapability(capability, structurePosition, facing);
	}
}
