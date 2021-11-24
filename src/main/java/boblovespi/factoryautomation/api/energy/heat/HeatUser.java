package boblovespi.factoryautomation.api.energy.heat;

import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Willi on 10/28/2018.
 */
public class HeatUser implements IHeatUser
{
	private float temperature;
	private float heatCapacity;
	private float conductivity;

	public HeatUser()
	{
		temperature = 0;
		heatCapacity = 0;
		conductivity = 0;
	}

	public HeatUser(float temperature, float heatCapacity, float conductivity)
	{
		this.temperature = temperature;
		this.heatCapacity = heatCapacity;
		this.conductivity = conductivity;
	}

	public void SetTemperature(float temperature)
	{
		this.temperature = temperature;
	}

	public void SetHeatCapacity(float mass)
	{
		this.heatCapacity = mass;
	}

	public void AddHeatCapacityAt300K(float toAdd)
	{
		if (toAdd <= 0)
			return;
		float totalEnergy = GetEnergy() + toAdd * 300;
		heatCapacity += toAdd;
		temperature = totalEnergy / heatCapacity;
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
	public float GetHeatCapacity()
	{
		return heatCapacity;
	}

	@Override
	public void TransferEnergy(float energyAmount)
	{
		temperature += (energyAmount / GetHeatCapacity());
		if (temperature < -273)
			temperature = -273;
	}

	@Override
	public float GetConductivity()
	{
		return conductivity;
	}

	public CompoundNBT WriteToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("temperature", temperature);
		nbt.putFloat("heatCapacity", heatCapacity);
		nbt.putFloat("conductivity", conductivity);
		return nbt;
	}

	public void ReadFromNBT(CompoundNBT tag)
	{
		temperature = tag.getFloat("temperature");
		heatCapacity = tag.getFloat("heatCapacity");
		conductivity = tag.getFloat("conductivity");
	}

	public void SetConductivity(float conductivity)
	{
		this.conductivity = conductivity;
	}
}
