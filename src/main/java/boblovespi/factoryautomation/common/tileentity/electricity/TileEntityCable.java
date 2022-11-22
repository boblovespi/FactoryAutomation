package boblovespi.factoryautomation.common.tileentity.electricity;

import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 12/1/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TileEntityCable extends BlockEntity implements ICapabilityProvider
{
	private final IEnergyStorage energyStorage;

	public TileEntityCable(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teCable.get(), pos, state);
		energyStorage = new EnergyStorage(128);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return LazyOptional.of(() -> (T) energyStorage);
		return super.getCapability(capability, facing);
	}
}
