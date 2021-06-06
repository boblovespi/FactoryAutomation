package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.util.Direction;

/**
 * Created by Willi on 1/15/2018.
 */
public interface IMechanicalUser
{
	boolean hasConnectionOnSide(Direction side);

	float getSpeedOnFace(Direction side);

	float getTorqueOnFace(Direction side);

	void setSpeedOnFace(Direction side, float speed);

	void setTorqueOnFace(Direction side, float torque);
}