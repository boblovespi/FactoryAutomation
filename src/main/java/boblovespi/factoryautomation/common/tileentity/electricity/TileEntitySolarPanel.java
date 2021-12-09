package boblovespi.factoryautomation.common.tileentity.electricity;

import boblovespi.factoryautomation.api.energy.electricity.EnergyConnection_;
import boblovespi.factoryautomation.api.energy.electricity.EnergyNetwork_;
import boblovespi.factoryautomation.api.energy.electricity.IProducesEnergy_;
import boblovespi.factoryautomation.api.energy.electricity.InternalEnergyStorage;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/21/2017.
 */
public class TileEntitySolarPanel extends BlockEntity
		implements IProducesEnergy_, ITickable, ICapabilityProvider
{
	private static final float productionScalar = 20f;
	private boolean hasTicked = false;
	private float energyProduction = 0;
	private float energyUsed = 0;
	private List<EnergyConnection_> energyConnections;
	private int cooldown = -1;
	private InternalEnergyStorage energyStorage;

	public TileEntitySolarPanel(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teSolarPanel, pos, state);
		energyConnections = new ArrayList<>(256);
		energyStorage = new InternalEnergyStorage(20);
	}

	@Override
	public EnergyNetwork_ GetNetwork()
	{
		return null;
	}

	@Override
	public boolean IsActive()
	{
		return true;
	}

	@Override
	public BlockEntity GetTe()
	{
		return this;
	}

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection_} to add to the machine
	 */
	@Override
	public void AddConnection(EnergyConnection_ connection)
	{
		energyConnections.add(connection);
		// EnergyNetwork_.GetFromWorld(world).AddConnection(connection);
	}

	/**
	 * Notify the machine that it needs to check all of its connections to see if they still exist
	 */
	@Override
	public void CheckConnections()
	{
		// TODO: 12/22/2017 FINISH
	}

	/**
	 * @return The amount of energy that the machine produces, including any extracted energy
	 */
	@Override
	public float AmountProduced()
	{
		return energyProduction;
	}

	/**
	 * @return The amount of energy produced minus the amount extracted
	 */
	@Override
	public float ActualAmountProduced()
	{
		return energyProduction - energyUsed;
	}

	/**
	 * @param amount   The amount of energy to extract
	 * @param simulate Whether or not to simulate the extraction; ie. if <code>simulate == true</code>, then no energy will actually be extracted
	 * @return The amount of energy extracted (or simulated extracted)
	 */
	@Override
	public float ExtractEnergy(float amount, boolean simulate)
	{
		ForceUpdate();
		float amountExtracted = Mth.clamp(amount, 0, energyProduction - energyUsed);
		if (!simulate)
		{
			energyUsed += amountExtracted;
		}
		return amountExtracted;
	}

	@Override
	public void tick()
	{
		hasTicked = false;
		if ((cooldown = ++cooldown % 20) == 0)
		{
			ForceUpdate();
		}
	}

	public void ForceUpdate()
	{
		if (level.isClientSide)
			return;
		if (hasTicked)
			return;
		hasTicked = true;
		if (level.canSeeSky(getBlockPos().above()))
		{
			energyProduction = productionScalar * (level.isDay() ? 1 : 0.1f);
		} else
		{
			energyProduction = 0;
		}

		energyUsed = 0;

		energyStorage.SetEnergy((int) energyProduction);

		// energyConnections.forEach(EnergyConnection_::Update);
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		energyProduction = compound.getFloat("energyProduction");
		energyUsed = compound.getFloat("energyUsed");
		//		CompoundNBT nbt = compound.getCompoundTag("connections");
		//		for (int i = 0; i < nbt.getSize(); i++)
		//		{
		//			energyConnections.add(NBTHelper.GetEnergyConnection(
		//					nbt.getCompoundTag(String.valueOf(i))));
		//		}
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.putFloat("energyProduction", energyProduction);
		compound.putFloat("energyUsed", energyUsed);
		//		CompoundNBT nbt = new CompoundNBT();
		//		for (int i = 0; i < energyConnections.size(); i++)
		//		{
		//			nbt.setTag(String.valueOf(i), energyConnections.get(i).ToNBT());
		//		}
		//		compound.setTag("connections", nbt);
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return LazyOptional.of(() -> (T) energyStorage);
		return super.getCapability(capability, facing);
	}
}
