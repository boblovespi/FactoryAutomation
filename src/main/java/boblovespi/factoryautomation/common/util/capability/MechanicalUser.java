package boblovespi.factoryautomation.common.util.capability;

import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 1/15/2018.
 */
@Deprecated
public class MechanicalUser implements IMechanicalUser
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
}
