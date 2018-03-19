package boblovespi.factoryautomation.common.tileentity.mechanical;

import boblovespi.factoryautomation.api.energy.EnergyConnection;
import boblovespi.factoryautomation.api.energy.IUsesEnergy;
import boblovespi.factoryautomation.common.util.capability.IMechanicalUser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 3/19/2018.
 */
public class TEMotor extends TileEntity implements IMechanicalUser, IUsesEnergy
{
	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return false;
	}

	@Override
	public float GetSpeedOnFace(EnumFacing side)
	{
		return 0;
	}

	@Override
	public float GetTorqueOnFace(EnumFacing side)
	{
		return 0;
	}

	@Override
	public void SetSpeedOnFace(EnumFacing side, float speed)
	{

	}

	@Override
	public void SetTorqueOnFace(EnumFacing side, float torque)
	{

	}

	@Override
	public boolean IsActive()
	{
		return false;
	}

	@Override
	public TileEntity GetTe()
	{
		return null;
	}

	/**
	 * Add a connection to the machine
	 *
	 * @param connection The {@link EnergyConnection} to add to the machine
	 */
	@Override
	public void AddConnection(EnergyConnection connection)
	{

	}

	/**
	 * Notify the machine that it needs to check all of its connections to see if they still exist
	 */
	@Override
	public void CheckConnections()
	{

	}
}
