package boblovespi.factoryautomation.common.tileentity.electricity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
		energyStorage = new EnergyStorage(128);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
			@Nullable EnumFacing facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return (T) energyStorage;
		return super.getCapability(capability, facing);
	}
}
