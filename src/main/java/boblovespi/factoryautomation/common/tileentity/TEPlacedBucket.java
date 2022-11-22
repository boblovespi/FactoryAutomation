package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 6/27/2018.
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEPlacedBucket extends BlockEntity
{
	private final FluidTank handler;

	public TEPlacedBucket(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.tePlacedBucket.get(), pos, state);
		handler = new FluidTank(1000)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
				level.sendBlockUpdated(worldPosition, state, state, 3);
			}
		};
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		handler.writeToNBT(compound);
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		handler.readFromNBT(compound);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (facing == Direction.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(()->(T) handler);
		return super.getCapability(capability, facing);
	}

	@Nullable
	public Fluid GetFluid()
	{
		if (handler.getFluidAmount() == handler.getCapacity())
			return handler.getFluid().getFluid();
		return null;
	}

	@Nullable
	public FluidStack GetFluidStack()
	{
		return handler.getFluid();
	}

	/**
	 * If the TileEntitySpecialRenderer associated with this TileEntity can be batched in with another renderers, and won't access the GL state.
	 * If TileEntity returns true, then TESR should have the same functionality as (and probably extend) the FastTESR class.
	 *
	 * Todo: translate to 1.16.5
	 */
	/*@Override
	public boolean hasFastRenderer()
	{
		return false;
	}*/
}
