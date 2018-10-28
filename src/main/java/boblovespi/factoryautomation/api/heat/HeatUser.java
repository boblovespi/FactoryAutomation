package boblovespi.factoryautomation.api.heat;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Willi on 10/28/2018.
 */
public class HeatUser implements IHeatUser
{
	private float temperature;
	private float mass;

	public HeatUser()
	{
		temperature = 0;
		mass = 0;
	}

	public HeatUser(float temperature, float mass)
	{
		this.temperature = temperature;
		this.mass = mass;
	}

	public void SetTemperature(float temperature)
	{
		this.temperature = temperature;
	}

	public void SetMass(float mass)
	{
		this.mass = mass;
	}

	/**
	 * @return the temperature in Celsius
	 */
	@Override
	public float GetTemperature()
	{
		return temperature;
	}

	@Override
	public float GetMass()
	{
		return mass;
	}

	public NBTTagCompound WriteToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("temperature", temperature);
		nbt.setFloat("mass", mass);
		return nbt;
	}

	public void ReadFromNBT(NBTTagCompound tag)
	{
		this.temperature = tag.getFloat("temperature");
		this.mass = tag.getFloat("mass");
	}
}
