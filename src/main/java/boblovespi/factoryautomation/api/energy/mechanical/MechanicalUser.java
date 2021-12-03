package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Willi on 8/13/2018.
 */
public class MechanicalUser implements IMechanicalUser
{
	private Set<Direction> sides;
	private float speed;
	private float torque;

	public MechanicalUser(EnumSet<Direction> sides)
	{
		this.sides = sides;
		speed = 0;
		torque = 0;
	}

	public MechanicalUser()
	{
		this.sides = EnumSet.noneOf(Direction.class);
		speed = 0;
		torque = 0;
	}

	public Set<Direction> GetSides()
	{
		return sides;
	}

	@Override
	public boolean HasConnectionOnSide(Direction side)
	{
		return GetSides().contains(side);
	}

	@Override
	public float GetSpeedOnFace(Direction side)
	{
		if (HasConnectionOnSide(side))
			return speed;
		return 0;
	}

	@Override
	public float GetTorqueOnFace(Direction side)
	{
		if (HasConnectionOnSide(side))
			return torque;
		return 0;
	}

	@Override
	public void SetSpeedOnFace(Direction side, float speed)
	{
		if (HasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void SetTorqueOnFace(Direction side, float torque)
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

	public CompoundTag WriteToNBT()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("speed", speed);
		nbt.putFloat("torque", torque);
		return nbt;
	}

	public void ReadFromNBT(CompoundTag tag)
	{
		this.speed = tag.getFloat("speed");
		this.torque = tag.getFloat("torque");
	}

	public void SetSides(EnumSet<Direction> sides)
	{
		this.sides = sides;
	}
}
