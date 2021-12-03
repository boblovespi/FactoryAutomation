package boblovespi.factoryautomation.api.energy.electricity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

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

	public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
	{
		super(capacity, maxReceive, maxExtract, energy);
	}

	public void SetEnergy(int amount)
	{
		energy = Mth.clamp(amount, 0, capacity);
	}

	public void PushEnergy(BlockEntity te, float maxPush, boolean allowPushToIUsesEnergy)
	{
		for (Direction dir : Direction.values())
		{
			BlockEntity te1 = te.getLevel().getBlockEntity(te.getBlockPos().relative(dir));
			if (te1 != null && (!(te1 instanceof IUsesEnergy_ || te1 instanceof IUsesEnergy) || allowPushToIUsesEnergy))
			{
				LazyOptional<IEnergyStorage> energy = te1.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
				energy.ifPresent(n -> n.receiveEnergy(extractEnergy((int) maxPush, false), false));
			}
		}
	}
}
