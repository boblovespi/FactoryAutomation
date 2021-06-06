package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.fluid.Pipe;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Willi on 10/7/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEPipe extends TileEntity implements ITickableTileEntity
{
	protected static int transferTime = 16;
	protected static int transferAmount = 1500;
	private int timer = -1;
	private final FluidTank tank;

	public TEPipe()
	{
		super(TileEntityHandler.tePipe);
		tank = new FluidTank(transferAmount)
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				BlockState state = Objects.requireNonNull(level).getBlockState(levelPosition);
				level.sendBlockUpdated(levelPosition, state, state, 7);
			}

			@Override
			public int fill(FluidStack resource, FluidAction doFill)
			{
				if (doFill.execute() && timer < 0)
					timer = transferTime;
				return super.fill(resource, doFill);
			}
		};
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
			return;

		// only decrease if we have fluids to process
		if (timer > 0)
		{
			timer--;
			if (timer <= 0)
			{
				// we *should* have no more fluids to process after this
				timer = -1;
				BlockState state = level.getBlockState(levelPosition);
				List<IFluidHandler> outputs = new ArrayList<>(6);

				for (Direction side : Direction.values())
				{
					if (!(state.getValue(Pipe.CONNECTIONS[side.ordinal()]).equals(Pipe.Connection.NONE)))
					{
						TileEntity te = level.getBlockEntity(levelPosition.relative(side));
						if (te != null)
						{
							LazyOptional<IFluidHandler> fluidHandler = te
									.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
							fluidHandler.ifPresent(outputs::add);
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
					FluidStack fluid = tank.drain(transferAmount, FluidAction.EXECUTE);
					int drained = output.fill(fluid.copy(), FluidAction.EXECUTE);
					fluid.shrink(drained);
					tank.fill(fluid, FluidAction.EXECUTE);
				}

				if (tank.getFluidAmount() > 0)
					timer = transferAmount; // again, we still have fluids to transfer!
			}
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		timer = tag.getInt("timer");
		tank.readFromNBT(tag.getCompound("tank"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.putInt("timer", timer);
		tag.put("tank", tank.writeToNBT(new CompoundNBT()));
		return super.save(tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) tank);
		return super.getCapability(capability, facing);
	}
}
