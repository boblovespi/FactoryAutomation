package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.fluid.Pipe;
import net.minecraft.block.state.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 10/7/2018.
 */
public class TEPipe extends TileEntity implements ITickable
{
	protected static int transferTime = 16;
	protected static int transferAmount = 1500;
	private int timer = -1;
	private FluidTank tank;

	public TEPipe()
	{
		tank = new FluidTank(transferAmount)
		{
			@Override
			protected void onContentsChanged()
			{
				markDirty();
				BlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 7);
			}

			@Override
			public int fill(FluidStack resource, boolean doFill)
			{
				if (doFill && timer < 0)
					timer = transferTime;
				return super.fill(resource, doFill);
			}
		};
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
			return;

		// only decrease if we have fluids to process
		if (timer > 0)
		{
			timer--;
			if (timer <= 0)
			{
				// we *should* have no more fluids to process after this
				timer = -1;
				BlockState state = FABlocks.pipe.ToBlock().getActualState(world.getBlockState(pos), world, pos);
				List<IFluidHandler> outputs = new ArrayList<>(6);

				for (Direction side : Direction.values())
				{
					if (!state.getValue(Pipe.CONNECTIONS[side.ordinal()]).equals(Pipe.Connection.NONE))
					{
						TileEntity te = world.getTileEntity(pos.offset(side));
						if (te != null)
						{
							IFluidHandler fluidHandler = te
									.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
							if (fluidHandler != null)
								outputs.add(fluidHandler);
						}
					}
				}

				// no outputs
				if (outputs.size() == 0)
				{
					if (tank.getFluidAmount() > 0)
						timer = transferTime; // we have fluids to transfer still, so keep trying to!
					return;
				}

				int transferAmount = tank.getFluidAmount() / outputs.size();

				for (IFluidHandler output : outputs)
				{
					FluidStack fluid = tank.drain(transferAmount, true);
					if (fluid == null)
						continue;
					int drained = output.fill(fluid.copy(), true);
					fluid.amount -= drained;
					tank.fillInternal(fluid, true);
				}

				if (tank.getFluidAmount() > 0)
					timer = transferAmount; // again, we still have fluids to transfer!
			}
		}
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		timer = tag.getInteger("timer");
		tank.readFromNBT(tag.getCompoundTag("tank"));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		tag.setInteger("timer", timer);
		tag.setTag("tank", tank.writeToNBT(new CompoundNBT()));
		return super.writeToNBT(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
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
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		return super.getCapability(capability, facing);
	}
}
