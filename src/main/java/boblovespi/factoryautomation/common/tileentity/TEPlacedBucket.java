package boblovespi.factoryautomation.common.tileentity;

import net.minecraft.block.state.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 6/27/2018.
 */
public class TEPlacedBucket extends TileEntity
{
	private FluidTank handler;

	public TEPlacedBucket()
	{
		handler = new FluidTank(Fluid.BUCKET_VOLUME)
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
	public CompoundNBT writeToNBT(CompoundNBT compound)
	{
		handler.writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(CompoundNBT compound)
	{
		super.readFromNBT(compound);
		handler.readFromNBT(compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getTileData()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		return (facing == Direction.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (facing == Direction.UP && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) handler;
		return super.getCapability(capability, facing);
	}

	public Fluid GetFluid()
	{
		if (handler.getFluidAmount() == handler.getCapacity() && handler.getFluid() != null)
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
	 */
	@Override
	public boolean hasFastRenderer()
	{
		return false;
	}
}
