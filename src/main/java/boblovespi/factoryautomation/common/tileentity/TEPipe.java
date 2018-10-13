package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.fluid.Pipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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
				IBlockState state = world.getBlockState(pos);
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
		if (timer > 0)
		{
			timer--;
			if (timer <= 0)
			{
				timer = -1;
				IBlockState state = FABlocks.pipe.ToBlock().getActualState(world.getBlockState(pos), world, pos);
				List<IFluidHandler> outputs = new ArrayList<>(6);

				for (EnumFacing side : EnumFacing.values())
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

				if (outputs.size() == 0)
					return;

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
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		timer = tag.getInteger("timer");
		tank.readFromNBT(tag.getCompoundTag("tank"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("timer", timer);
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return super.writeToNBT(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		return super.getCapability(capability, facing);
	}
}
