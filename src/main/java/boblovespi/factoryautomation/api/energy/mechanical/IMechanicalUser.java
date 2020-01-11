package boblovespi.factoryautomation.api.energy.mechanical;

import net.minecraft.util.Direction;

/**
 * Created by Willi on 1/15/2018.
 */
public interface IMechanicalUser
{
	boolean HasConnectionOnSide(Direction side);

	float GetSpeedOnFace(Direction side);

	float GetTorqueOnFace(Direction side);

	void SetSpeedOnFace(Direction side, float speed);

	void SetTorqueOnFace(Direction side, float torque);
}