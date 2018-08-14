package boblovespi.factoryautomation.api.mechanical;

import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 8/13/2018.
 */
public class MechanicalUser implements IMechanicalUser
{
	private EnumFacing side;
	private float speed;
	private float torque;

	public MechanicalUser(EnumFacing side)
	{
		this.side = side;
		speed = 0;
		torque = 0;
	}

	MechanicalUser()
	{
		this.side = EnumFacing.NORTH;
		speed = 0;
		torque = 0;
	}

	public EnumFacing GetSide()
	{
		return side;
	}

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return GetSide() == side;
	}

	@Override
	public float GetSpeedOnFace(EnumFacing side)
	{
		if (HasConnectionOnSide(side))
			return speed;
		return 0;
	}

	@Override
	public float GetTorqueOnFace(EnumFacing side)
	{
		if (HasConnectionOnSide(side))
			return torque;
		return 0;
	}

	@Override
	public void SetSpeedOnFace(EnumFacing side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(EnumFacing side, float torque)
	{
		if (HasConnectionOnSide(side))
			this.torque = torque;
	}
}
