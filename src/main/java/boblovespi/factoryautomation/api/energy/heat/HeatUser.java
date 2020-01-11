package boblovespi.factoryautomation.api.energy.heat;

import boblovespi.factoryautomation.api.energy.EnergyConstants;
import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Willi on 10/28/2018.
 */
public class HeatUser implements IHeatUser
{
	private float temperature;
	private float substanceAmount;
	private float conductivity;

	public HeatUser()
	{
		temperature = 0;
		substanceAmount = 0;
		conductivity = 0;
	}

	public HeatUser(float temperature, float substanceAmount, float conductivity)
	{
		this.temperature = temperature;
		this.substanceAmount = substanceAmount;
		this.conductivity = conductivity;
	}

	public void SetTemperature(float temperature)
	{
		this.temperature = temperature;
	}

	public void SetSubstanceAmount(float mass)
	{
		this.substanceAmount = mass;
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
	public float GetSubstanceAmount()
	{
		return substanceAmount;
	}

	@Override
	public void TransferEnergy(float energyAmount)
	{
		temperature += (energyAmount / EnergyConstants.GAS_CONSTANT / GetSubstanceAmount());
		if (temperature < 0)
			temperature = 0;
	}

	@Override
	public float GetConductivity()
	{
		return conductivity;
	}

	public CompoundNBT WriteToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.setFloat("temperature", temperature);
		nbt.setFloat("substanceAmount", substanceAmount);
		nbt.setFloat("conductivity", conductivity);
		return nbt;
	}

	public void ReadFromNBT(CompoundNBT tag)
	{
		temperature = tag.getFloat("temperature");
		substanceAmount = tag.getFloat("substanceAmount");
		conductivity = tag.getFloat("conductivity");
	}

	public void SetConductivity(float conductivity)
	{
		this.conductivity = conductivity;
	}
}
