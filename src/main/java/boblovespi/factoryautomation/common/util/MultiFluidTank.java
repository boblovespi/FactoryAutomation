package boblovespi.factoryautomation.common.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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

	/**
	 * Returns an array of objects which represent the internal tanks.
	 * These objects cannot be used to manipulate the internal tanks.
	 *
	 * @return Properties for the relevant internal tanks.
	 */
	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return FluidTankProperties.convert(
				tanks.stream().map(FluidTank::getInfo)
					 .collect(Collectors.toList())
					 .toArray(new FluidTankInfo[0]));
	}

	/**
	 * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
	 *
	 * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
	 * @param doFill   If false, fill will only be simulated.
	 * @return Amount of resource that was (or would have been, if simulated) filled.
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		int amount = resource.amount;
		for (FluidTank tank : tanks)
		{
			amount -= tank.fill(resource, doFill);
			resource.amount = amount;
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
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		int maxAmount = resource.amount;
		int amountDrained = 0;

		for (FluidTank tank : tanks)
		{
			FluidStack drained = tank.drain(resource, doDrain);
			if (drained != null)
			{
				maxAmount -= drained.amount;
				amountDrained += drained.amount;
			}
			resource.amount = maxAmount;
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
	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		return null;
	}
}
