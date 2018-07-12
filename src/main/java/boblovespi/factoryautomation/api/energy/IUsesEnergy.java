package boblovespi.factoryautomation.api.energy;

import boblovespi.factoryautomation.api.energy.enums.VoltageTier;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by Willi on 7/9/2018.
 */
public interface IUsesEnergy
{
	/**
	 * Returns whether the machine is active
	 */
	boolean IsActive();

	/**
	 * Gets the TileEntity of the machine for internal use
	 */
	TileEntity GetTe();

	/**
	 * Gets the voltage tier of the machine
	 */
	VoltageTier GetVoltageTier();

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

	/**
	 * Gets the list of all energy connections of the machine
	 */
	List<EnergyConnection> GetConnections();
}

