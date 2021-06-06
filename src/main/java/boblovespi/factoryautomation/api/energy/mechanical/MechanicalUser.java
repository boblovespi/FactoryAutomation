package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

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
	public boolean hasConnectionOnSide(Direction side)
	{
		return GetSides().contains(side);
	}

	@Override
	public float getSpeedOnFace(Direction side)
	{
		if (hasConnectionOnSide(side))
			return speed;
		return 0;
	}

	@Override
	public float getTorqueOnFace(Direction side)
	{
		if (hasConnectionOnSide(side))
			return torque;
		return 0;
	}

	@Override
	public void setSpeedOnFace(Direction side, float speed)
	{
		if (hasConnectionOnSide(side))
			this.speed = speed;
	}

	@Override
	public void setTorqueOnFace(Direction side, float torque)
	{
		if (hasConnectionOnSide(side))
			this.torque = torque;
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getTorque()
	{
		return torque;
	}

	public CompoundNBT saveToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("speed", speed);
		nbt.putFloat("torque", torque);
		return nbt;
	}

	public void loadFromNBT(CompoundNBT tag)
	{
		this.speed = tag.getFloat("speed");
		this.torque = tag.getFloat("torque");
	}

	public void setSides(EnumSet<Direction> sides)
	{
		this.sides = sides;
	}
}
