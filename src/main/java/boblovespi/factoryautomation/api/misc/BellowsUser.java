package boblovespi.factoryautomation.api.misc;

import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Willi on 4/27/2019.
 */
public class BellowsUser implements IBellowsable
{
	private final float baseEfficiency;
	private float efficiency;
	private int time;
	private int maxTime;

	public BellowsUser(float baseEfficiency)
	{
		this.baseEfficiency = baseEfficiency;
		efficiency = baseEfficiency;
		time = 0;
	}

	@Override
	public void Blow(float efficiency, int time)
	{
		this.efficiency = efficiency;
		this.time = time;
		maxTime = time;
	}

	public float GetEfficiency()
	{
		return efficiency;
	}

	public int GetTime()
	{
		return time;
	}

	public void Tick()
	{
		if (time > 0)
		{
			time--;
			if (time <= 0)
			{
				efficiency = baseEfficiency;
				time = 0;
				maxTime = 1;
			}
		}
	}

	public CompoundNBT WriteToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.setFloat("efficiency", efficiency);
		nbt.setInteger("time", time);
		nbt.setInteger("maxTime", maxTime);
		return nbt;
	}

	public void ReadFromNBT(CompoundNBT tag)
	{
		efficiency = tag.getFloat("efficiency");
		time = tag.getInteger("time");
		maxTime = tag.getInteger("maxTime");
	}

	public int GetMaxTime()
	{
		return maxTime;
	}
}
