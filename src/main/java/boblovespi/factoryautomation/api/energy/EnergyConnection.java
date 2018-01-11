package boblovespi.factoryautomation.api.energy;

import boblovespi.factoryautomation.common.util.DimLocation;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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

	public EnergyConnection(IProducesEnergy producer, IRequiresEnergy requirer,
			int distance, float decay, float maxTransfer)
	{
		source = producer;
		consumer = requirer;
		this.distance = distance;
		distanceEnergyDecay = decay;
		maxEnergyTransfer = maxTransfer;
	}

	public EnergyConnection()
	{
	}

	@Nullable
	public static EnergyConnection FromNBT(NBTTagCompound tag, World world)
	{
		EnergyConnection en = new EnergyConnection();
		DimLocation loc1 = NBTHelper.GetLocationTag(tag, "sourceLoc");

		TileEntity te1 = null;
		IProducesEnergy source = null;

		if (loc1 != null)
		{
			te1 = world.getTileEntity(loc1.getPos());
		}

		if (te1 != null && te1 instanceof IProducesEnergy)
		{
			source = (IProducesEnergy) te1;
		}

		en.source = source;

		DimLocation loc2 = NBTHelper.GetLocationTag(tag, "sourceLoc");

		TileEntity te2 = null;
		IRequiresEnergy consumer = null;

		if (loc2 != null)
		{
			te2 = world.getTileEntity(loc2.getPos());
		}

		if (te2 != null && te2 instanceof IRequiresEnergy)
		{
			consumer = (IRequiresEnergy) te2;
		}

		en.consumer = consumer;

		en.distance = tag.getInteger("distance");
		en.distanceEnergyDecay = tag.getFloat("distanceDecay");
		en.maxEnergyTransfer = tag.getFloat("maxTransfer");

		return en;
	}

	public NBTTagCompound ToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		NBTHelper.SetLocationTag(tag, "sourceLoc",
								 source.GetTe().getWorld().getWorldType()
									   .getId(), source.GetTe().getPos().getX(),
								 source.GetTe().getPos().getY(),
								 source.GetTe().getPos().getX());
		NBTHelper.SetLocationTag(tag, "consumerLoc",
								 consumer.GetTe().getWorld().getWorldType()
										 .getId(),
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
		float amountNeededToTransfer = consumer.ActualAmountNeeded() / (distance
				* distanceEnergyDecay);

		float amountExtracted = source
				.ExtractEnergy(amountNeededToTransfer, false);

		consumer.InsertEnergy(
				amountExtracted * (distance * distanceEnergyDecay), false);
	}

	@Override
	public boolean equals(Object ob)
	{
		if (!(ob instanceof EnergyConnection))
			return false;
		EnergyConnection obj = (EnergyConnection) ob;
		return obj.consumer.equals(consumer) && obj.source.equals(source)
				&& obj.distanceEnergyDecay == distanceEnergyDecay
				&& obj.distance == distance
				&& obj.maxEnergyTransfer == maxEnergyTransfer;
	}
}
