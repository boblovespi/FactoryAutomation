package boblovespi.factoryautomation.common.util.capability;

import net.minecraft.util.EnumFacing;

/**
 * Created by Willi on 1/15/2018.
 */
public interface IMechanicalUser
{
	boolean HasConnectionOnSide(EnumFacing side);

	float GetSpeedOnFace(EnumFacing side);

	float GetTorqueOnFace(EnumFacing side);

	void SetSpeedOnFace(EnumFacing side, float speed);

	void SetTorqueOnFace(EnumFacing side, float torque);
}
