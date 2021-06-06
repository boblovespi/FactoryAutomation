package boblovespi.factoryautomation.api.misc;

import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Willi on 4/27/2019.
 */
public class bellowsUser implements IBellowsable
{
	private final float baseEfficiency;
	private float efficiency;
	private int time;
	private int maxTime;

	public bellowsUser(float baseEfficiency)
	{
		this.baseEfficiency = baseEfficiency;
		efficiency = baseEfficiency;
		time = 0;
	}

	@Override
	public void blow(float efficiency, int time)
	{
		this.efficiency = efficiency;
		this.time = time;
		maxTime = time;
	}

	public float getEfficiency()
	{
		return efficiency;
	}

	public int getTime()
	{
		return time;
	}

	public void tick()
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

	public CompoundNBT writeToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("efficiency", efficiency);
		nbt.putInt("time", time);
		nbt.putInt("maxTime", maxTime);
		return nbt;
	}

	public void readFromNBT(CompoundNBT tag)
	{
		efficiency = tag.getFloat("efficiency");
		time = tag.getInt("time");
		maxTime = tag.getInt("maxTime");
	}

	public int getMaxTime()
	{
		return maxTime;
	}
}
