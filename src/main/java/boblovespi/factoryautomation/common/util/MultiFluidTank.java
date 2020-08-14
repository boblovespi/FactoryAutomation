package boblovespi.factoryautomation.common.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.graalvm.compiler.api.replacements.Snippet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Willi on 12/24/2017.
 */
public class MultiFluidTank implements IFluidHandler
{
	private List<FluidTank> tanks;

	public MultiFluidTank(int tankCount, int tankSize)
	{
		tanks = new ArrayList<>(tankCount);
		for (int i = 0; i < tankCount; i++)
		{
			tanks.add(new FluidTank(tankSize));
		}
	}

	@Override
	public int getTanks()
	{
		return tanks.size();
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank)
	{
		return tanks.get(tank).getFluid();
	}

	@Override
	public int getTankCapacity(int tank)
	{
		return tanks.get(tank).getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
	{
		return tanks.get(tank).isFluidValid(stack);
	}

	/**
	 * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
	 *
	 * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
	 * @param action   If SIMULATE, fill will only be simulated.
	 * @return Amount of resource that was (or would have been, if simulated) filled.
	 */
	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		int amount = resource.getAmount();
		for (FluidTank tank : tanks)
		{
			amount -= tank.fill(resource, action);
			if (action.execute())
				resource.setAmount(amount);
		}
		return amount;
	}

	/**
	 * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
	 *
	 * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
	 * @param doDrain  If false, drain will only be simulated.
	 * @return FluidStack representing the Fluid and amount that was (or would have been, if
	 * simulated) drained.
	 */
	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, FluidAction doDrain)
	{
		int maxAmount = resource.getAmount();
		int amountDrained = 0;

		for (FluidTank tank : tanks)
		{
			FluidStack drained = tank.drain(resource, doDrain);
			if (!drained.isEmpty())
			{
				maxAmount -= drained.getAmount();
				amountDrained += drained.getAmount();
			}
		}
		return new FluidStack(resource.getFluid(), amountDrained);
	}

	/**
	 * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
	 * <p/>
	 * This method is not Fluid-sensitive.
	 *
	 * @param maxDrain Maximum amount of fluid to drain.
	 * @param doDrain  If false, drain will only be simulated.
	 * @return FluidStack representing the Fluid and amount that was (or would have been, if
	 * simulated) drained.
	 */
	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction doDrain)
	{
		int amountDrained = 0;
		FluidStack resource = FluidStack.EMPTY;
		for (FluidTank tank : tanks)
		{
			FluidStack drained = tank.drain(new FluidStack(resource.getFluid(), maxDrain), doDrain);
			if (!drained.isEmpty())
			{
				if (resource.isEmpty())
					resource = drained;
				maxDrain -= drained.getAmount();
				amountDrained += drained.getAmount();
			}
		}
		return new FluidStack(resource.getFluid(), amountDrained);
	}

	/**
	 * @return A list of all the fluids in the tank.  If tanks are empty, they are not added
	 */
	public List<FluidStack> GetFluids()
	{
		return tanks.stream().filter(t -> t.getFluidAmount() != 0).map(FluidTank::getFluid)
					.collect(Collectors.toList());
	}
}
