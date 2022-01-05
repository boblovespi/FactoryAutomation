package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TEWoodenTank extends BlockEntity
{
	private static int bucketsCanHold = 4;
	private final FluidTank handler;

	public TEWoodenTank(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teWoodenTank, pos, state);
		handler = new FluidTank(bucketsCanHold * 1000)
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
	public void load(CompoundTag compound)
	{
		super.load(compound);
		handler.readFromNBT(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		handler.writeToNBT(compound);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> handler).cast();
		return super.getCapability(capability, facing);
	}

	public FluidStack GetFluidStack()
	{
		return handler.getFluid();
	}

	public FluidTank GetHandler()
	{
		return handler;
	}
}
