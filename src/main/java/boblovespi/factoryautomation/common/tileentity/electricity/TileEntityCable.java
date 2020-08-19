package boblovespi.factoryautomation.common.tileentity.electricity;

import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/1/2017.
 */
public class TileEntityCable extends TileEntity implements ICapabilityProvider
{
	private IEnergyStorage energyStorage;

	public TileEntityCable()
	{
		super(TileEntityHandler.teCable);
		energyStorage = new EnergyStorage(128);
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return LazyOptional.of(() -> (T) energyStorage);
		return super.getCapability(capability, facing);
	}
}
