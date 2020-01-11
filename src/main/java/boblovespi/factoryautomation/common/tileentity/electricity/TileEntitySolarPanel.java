package boblovespi.factoryautomation.common.tileentity.electricity;

import boblovespi.factoryautomation.api.energy.electricity.EnergyConnection_;
import boblovespi.factoryautomation.api.energy.electricity.EnergyNetwork_;
import boblovespi.factoryautomation.api.energy.electricity.IProducesEnergy_;
import boblovespi.factoryautomation.api.energy.electricity.InternalEnergyStorage;
import net.minecraft.block.state.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/21/2017.
 */
public class TileEntitySolarPanel extends TileEntity
		implements IProducesEnergy_, ITickable, ICapabilityProvider
{
	private static final float productionScalar = 20f;
	private boolean hasTicked = false;
	private float energyProduction = 0;
	private float energyUsed = 0;
	private List<EnergyConnection_> energyConnections;
	private int cooldown = -1;
	private InternalEnergyStorage energyStorage;

	public TileEntitySolarPanel()
	{
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
	public TileEntity GetTe()
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
		float amountExtracted = MathHelper
				.clamp(amount, 0, energyProduction - energyUsed);
		if (!simulate)
		{
			energyUsed += amountExtracted;
		}
		return amountExtracted;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		hasTicked = false;
		if ((cooldown = ++cooldown % 20) == 0)
		{
			ForceUpdate();
		}
	}

	public void ForceUpdate()
	{
		if (world.isRemote)
			return;
		if (hasTicked)
			return;
		hasTicked = true;
		if (world.canBlockSeeSky(pos.up()))
		{
			energyProduction =
					productionScalar * world.getSunBrightnessFactor(0f);
		} else
		{
			energyProduction = 0;
		}

		energyUsed = 0;

		energyStorage.SetEnergy((int) energyProduction);

		// energyConnections.forEach(EnergyConnection_::Update);
		markDirty();
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public void readFromNBT(CompoundNBT compound)
	{
		super.readFromNBT(compound);
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
	public CompoundNBT writeToNBT(CompoundNBT compound)
	{
		super.writeToNBT(compound);
		compound.setFloat("energyProduction", energyProduction);
		compound.setFloat("energyUsed", energyUsed);
		//		CompoundNBT nbt = new CompoundNBT();
		//		for (int i = 0; i < energyConnections.size(); i++)
		//		{
		//			nbt.setTag(String.valueOf(i), energyConnections.get(i).ToNBT());
		//		}
		//		compound.setTag("connections", nbt);
		return compound;
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	/**
	 * Gets a {@link CompoundNBT} that can be used to store custom data for this tile entity.
	 * It will be written, and read from disc, so it persists over world saves.
	 *
	 * @return A compound tag for custom data
	 */
	@Override
	public CompoundNBT getTileData()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public boolean hasCapability(Capability<?> capability,
			@Nullable Direction facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
			@Nullable Direction facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
			return (T) energyStorage;
		return null;
	}
}
