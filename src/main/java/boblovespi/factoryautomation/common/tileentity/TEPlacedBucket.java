package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/27/2018.
 */
public class TEPlacedBucket extends TileEntity
{
	private FluidTank handler;

	public TEPlacedBucket()
	{
		super(TileEntityHandler.tePlacedBucket);
		handler = new FluidTank(1000)
		{
			@Override
			protected void onContentsChanged()
			{
				markDirty();
				BlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);
			}
		};
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		handler.writeToNBT(compound);
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		super.read(state, compound);
		handler.readFromNBT(compound);
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (facing == Direction.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(()->(T) handler);
		return super.getCapability(capability, facing);
	}

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
}
