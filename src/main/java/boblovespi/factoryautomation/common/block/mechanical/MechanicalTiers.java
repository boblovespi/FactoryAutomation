package boblovespi.factoryautomation.common.block.mechanical;

public enum MechanicalTiers
{
	WOOD(10, 10), IRON(25, 25);
	public final float maxSpeed;
	public final float maxTorque;

	MechanicalTiers(float maxSpeed, float maxTorque)
	{
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
	}
}
