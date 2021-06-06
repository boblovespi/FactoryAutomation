package boblovespi.factoryautomation.common.tileentity;

import mcp.MethodsReturnNonnullByDefault;
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
public class TEPlacedBucket extends TileEntity
{
	private final FluidTank handler;

	public TEPlacedBucket()
	{
		super(TileEntityHandler.tePlacedBucket);
		handler = new FluidTank(1000)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				BlockState state = Objects.requireNonNull(world).getBlockState(levelPosition);
				world.sendBlockUpdated(levelPosition, state, state, 3);
			}
		};
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		handler.writeToNBT(compound);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
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
	@Override
	public boolean hasFastRenderer()
	{
		return false;
	}
}
