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

	public void setTemperature(float temperature)
	{
		this.temperature = temperature;
	}

	public void setSubstanceAmount(float mass)
	{
		this.substanceAmount = mass;
	}

	/**
	 * @return the temperature in Celsius
	 */
	@Override
	public float getTemperature()
	{
		return temperature;
	}

	@Override
	public float getSubstanceAmount()
	{
		return substanceAmount;
	}

	@Override
	public void transferEnergy(float energyAmount)
	{
		temperature += (energyAmount / EnergyConstants.GAS_CONSTANT / getSubstanceAmount());
		if (temperature < 0)
			temperature = 0;
	}

	@Override
	public float getConductivity()
	{
		return conductivity;
	}

	public CompoundNBT saveToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("temperature", temperature);
		nbt.putFloat("substanceAmount", substanceAmount);
		nbt.putFloat("conductivity", conductivity);
		return nbt;
	}

	public void loadFromNBT(CompoundNBT tag)
	{
		temperature = tag.getFloat("temperature");
		substanceAmount = tag.getFloat("substanceAmount");
		conductivity = tag.getFloat("conductivity");
	}

	public void setConductivity(float conductivity)
	{
		this.conductivity = conductivity;
	}
}
