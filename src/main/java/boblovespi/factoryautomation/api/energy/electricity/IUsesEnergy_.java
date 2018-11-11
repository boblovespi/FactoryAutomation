package boblovespi.factoryautomation.api.energy.electricity;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IUsesEnergy_
{
	@Deprecated
	default EnergyNetwork_ GetNetwork()
	{
		return null;
	}

	@Deprecated
	default IUsesEnergy_ SetNetwork(EnergyNetwork_ network)
	{
		return null;
	}

	boolean IsActive();

	TileEntity GetTe();

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection_} to add to the machine
	 */
	void AddConnection(EnergyConnection_ connection);

	/**
	 * Notify the machine that it needs to check all of its connections to see if they still exist
	 */
	void CheckConnections();
}
