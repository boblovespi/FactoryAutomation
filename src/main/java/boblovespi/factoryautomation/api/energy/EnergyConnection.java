package boblovespi.factoryautomation.api.energy;

import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Willi on 12/17/2017.
 */
public class EnergyConnection
{
	IProducesEnergy source;
	IRequiresEnergy consumer;

	int distance;
	float distanceEnergyDecay;
	float maxEnergyTransfer;

	public NBTTagCompound ToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTHelper.SetLocationTag(tag, "sourceLoc", 0,
								 source.GetTe().getPos().getX(),
								 source.GetTe().getPos().getY(),
								 source.GetTe().getPos().getX());
		NBTHelper.SetLocationTag(tag, "consumerLoc", 0,
								 consumer.GetTe().getPos().getX(),
								 consumer.GetTe().getPos().getY(),
								 consumer.GetTe().getPos().getX());
		tag.setInteger("distance", distance);
		tag.setFloat("distanceDecay", distanceEnergyDecay);
		tag.setFloat("maxTransfer", maxEnergyTransfer);
		return tag;
	}

	public void Update()
	{
		// TODO: write
	}

}
