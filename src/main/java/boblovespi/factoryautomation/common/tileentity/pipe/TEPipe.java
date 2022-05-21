package boblovespi.factoryautomation.common.tileentity.pipe;

import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 10/7/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEPipe extends BlockEntity implements ITickable
{
	protected static int transferTime = 16;
	protected static int transferAmount = 1500;
	private final FluidTank[] tanks;
	private final IONode[] ioNodes;
	private int timer = -1;
	private PipeNetwork network = null;
	private boolean shouldSaveNet = false;

	public TEPipe(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.tePipe, pos, state);
		tanks = new FluidTank[6];
		for (int i = 0; i < 6; i++)
		{
			tanks[i] = new FluidTank(transferAmount)
			{
				@Override
				public int fill(FluidStack resource, FluidAction doFill)
				{
					if (doFill.execute() && timer < 0)
						timer = transferTime;
					return super.fill(resource, doFill);
				}

				@Override
				protected void onContentsChanged()
				{
					setChanged();
					BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
					level.sendBlockUpdated(worldPosition, state, state, 7);
				}
			};
		}
		ioNodes = new IONode[6];
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		if (network != null)
			network.Load(level);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
			return;
		if (shouldSaveNet)
			network.Tick();
		for (var ioNode : ioNodes)
		{
			if (ioNode != null)
			{
				var te = level.getBlockEntity(worldPosition.relative(ioNode.Facing()));
				if (ioNode.IsOutput() && te != null)
				{
					var optional = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
													ioNode.Facing().getOpposite());
					optional.ifPresent(n -> {
						var stack = new FluidStack(network.nodeNetwork.fluid, Mth.clamp(ioNode.OutputBuffer(), 0, network.nodeNetwork.ioRate));
						var fill = n.fill(stack, IFluidHandler.FluidAction.EXECUTE);
						ioNode.DrainOutput(fill);
					});
				}
				else if (ioNode.IsInput() && te != null)
				{
					var optional = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
													ioNode.Facing().getOpposite());
					optional.ifPresent(n -> {
						var stack = n.drain(network.nodeNetwork.ioRate, IFluidHandler.FluidAction.SIMULATE);
						if (network.nodeNetwork.fluid == Fluids.EMPTY)
							network.nodeNetwork.fluid = stack.getFluid();
						if (network.nodeNetwork.fluid == stack.getFluid())
						{
							var filled = ioNode.AddToInBuffer(stack.getAmount());
							var actualDrain = new FluidStack(stack, filled);
							n.drain(actualDrain, IFluidHandler.FluidAction.EXECUTE);
						}
					});
				}
			}
		}

		// only decrease if we have fluids to process
		/*if (timer > 0)
		{
			timer--;
			if (timer <= 0)
			{
				// we *should* have no more fluids to process after this
				timer = -1;
				BlockState state = level.getBlockState(worldPosition);
				List<IFluidHandler> outputs = new ArrayList<>(6);

				for (Direction side : Direction.values())
				{
					if (!(state.getValue(Pipe.CONNECTIONS[side.ordinal()]).equals(Pipe.Connection.NONE)))
					{
						BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
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
		}*/
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		timer = tag.getInt("timer");
		for (int i = 0; i < tanks.length; i++)
		{
			FluidTank tank = tanks[i];
			tank.readFromNBT(tag.getCompound("tank_" + i));
		}
		if (tag.contains("pipenet"))
		{
			shouldSaveNet = true;
			network = PipeNetwork.FromNBT(worldPosition, tag.getCompound("pipenet"));
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("timer", timer);
		for (int i = 0; i < tanks.length; i++)
		{
			tag.put("tank_" + i, tanks[i].writeToNBT(new CompoundTag()));
		}
		if (shouldSaveNet)
			tag.put("pipenet", network.ToNBT());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null)
			return LazyOptional.of(() -> (T) tanks[facing.ordinal()]);
		return super.getCapability(capability, facing);
	}

	public PipeNetwork GetPipeNetwork()
	{
		return network;
	}

	public void SetPipeNetwork(PipeNetwork net, PipeNetwork.Adjacencies adj)
	{
		if (net != network)
		{
			if (network != null)
				network.LeaveNode(worldPosition);
			network = net;
			net.AddNode(worldPosition, adj);
		}
		shouldSaveNet = net.GetDataSaver().equals(worldPosition);
	}

	public void SetPipeNetworkQuickly(PipeNetwork net)
	{
		network = net;
	}

	public void AddIONode(Direction side)
	{
		if (network != null)
		{
			var ioNode = new IONode(network.nodeNetwork, worldPosition, side);
			if (side == Direction.UP)
				ioNode.SetInput(true);
			ioNodes[side.ordinal()] = ioNode;
		}
	}
}
