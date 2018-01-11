package boblovespi.factoryautomation.api.energy;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Willi on 4/12/2017.
 */
public interface IUsesEnergy
{
	@Deprecated
	default EnergyNetwork GetNetwork()
	{
		return null;
	}

	@Deprecated
	default IUsesEnergy SetNetwork(EnergyNetwork network)
	{
		return null;
	}

	boolean IsActive();

	TileEntity GetTe();

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection} to add to the machine
	 */
	void AddConnection(EnergyConnection connection);

	/**
	 * Notify the machine that it needs to check all of its connections to see if they still exist
	 */
	void CheckConnections();
}
