package boblovespi.factoryautomation.common.tileentity.electricity;

import boblovespi.factoryautomation.api.energy.EnergyConnection;
import boblovespi.factoryautomation.api.energy.EnergyNetwork;
import boblovespi.factoryautomation.api.energy.IProducesEnergy;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/21/2017.
 */
public class TileEntitySolarPanel extends TileEntity
		implements IProducesEnergy, ITickable
{
	private static final float productionScalar = 20f;

	private float energyProduction = 0;
	private float energyUsed = 0;
	private List<EnergyConnection> energyConnections;
	private int cooldown = -1;

	public TileEntitySolarPanel()
	{
		energyConnections = new ArrayList<>(256);
	}

	@Override
	public EnergyNetwork GetNetwork()
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
	 * @param connection The {@link EnergyConnection} to add to the machine
	 */
	@Override
	public void AddConnection(EnergyConnection connection)
	{
		energyConnections.add(connection);
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
	 * @return Whether that much energy can be extracted or not, if true and <code>simulate == false</code>, then energy was extracted
	 */
	@Override
	public boolean ExtractEnergy(float amount, boolean simulate)
	{
		ForceUpdate();
		boolean canExtract = amount + energyUsed <= energyProduction;
		if (canExtract && !simulate)
		{
			energyUsed += amount;
		}
		return canExtract;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if ((cooldown = ++cooldown % 20) == 0)
		{
			ForceUpdate();
		}
	}

	public void ForceUpdate()
	{
		if (world.isRemote)
			return;
		if (world.canBlockSeeSky(pos.up()))
		{
			energyProduction =
					productionScalar * world.getSunBrightnessFactor(0f);
		} else
		{
			energyProduction = 0;
		}
		energyConnections.forEach(EnergyConnection::Update);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		energyProduction = compound.getFloat("energyProduction");
		energyUsed = compound.getFloat("energyUsed");
		NBTTagCompound nbt = compound.getCompoundTag("connections");
		for (int i = 0; i < nbt.getSize(); i++)
		{
			energyConnections.add(NBTHelper.GetEnergyConnection(
					nbt.getCompoundTag(String.valueOf(i))));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setFloat("energyProduction", energyProduction);
		compound.setFloat("energyUsed", energyUsed);
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < energyConnections.size(); i++)
		{
			nbt.setTag(String.valueOf(i), energyConnections.get(i).ToNBT());
		}
		compound.setTag("connections", nbt);
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
	 * Gets a {@link NBTTagCompound} that can be used to store custom data for this tile entity.
	 * It will be written, and read from disc, so it persists over world saves.
	 *
	 * @return A compound tag for custom data
	 */
	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
}
