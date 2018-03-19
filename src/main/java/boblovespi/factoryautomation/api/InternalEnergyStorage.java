package boblovespi.factoryautomation.api;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Created by Willi on 3/18/2018.
 */
public class InternalEnergyStorage extends EnergyStorage
{
	public InternalEnergyStorage(int capacity)
	{
		super(capacity);
	}

	public InternalEnergyStorage(int capacity, int maxTransfer)
	{
		super(capacity, maxTransfer);
	}

	public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract)
	{
		super(capacity, maxReceive, maxExtract);
	}

	public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract,
			int energy)
	{
		super(capacity, maxReceive, maxExtract, energy);
	}

	public void SetEnergy(int amount)
	{
		energy = MathHelper.clamp(amount, 0, capacity);
	}
}
