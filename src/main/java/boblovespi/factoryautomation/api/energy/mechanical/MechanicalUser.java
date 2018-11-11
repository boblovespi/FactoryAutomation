package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Willi on 8/13/2018.
 */
public class MechanicalUser implements IMechanicalUser
{
	private Set<EnumFacing> sides;
	private float speed;
	private float torque;

	public MechanicalUser(EnumSet<EnumFacing> sides)
	{
		this.sides = sides;
		speed = 0;
		torque = 0;
	}

	public MechanicalUser()
	{
		this.sides = EnumSet.noneOf(EnumFacing.class);
		speed = 0;
		torque = 0;
	}

	public Set<EnumFacing> GetSides()
	{
		return sides;
	}

	@Override
	public boolean HasConnectionOnSide(EnumFacing side)
	{
		return GetSides().contains(side);
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

	public float GetSpeed()
	{
		return speed;
	}

	public float GetTorque()
	{
		return torque;
	}

	public NBTTagCompound WriteToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("speed", speed);
		nbt.setFloat("torque", torque);
		return nbt;
	}

	public void ReadFromNBT(NBTTagCompound tag)
	{
		this.speed = tag.getFloat("speed");
		this.torque = tag.getFloat("torque");
	}

	public void SetSides(EnumSet<EnumFacing> sides)
	{
		this.sides = sides;
	}
}
