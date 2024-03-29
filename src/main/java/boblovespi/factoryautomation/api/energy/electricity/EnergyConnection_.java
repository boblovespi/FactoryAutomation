package boblovespi.factoryautomation.api.energy.electricity;

import boblovespi.factoryautomation.common.util.DimLocation;
import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/17/2017.
 */
public class EnergyConnection_
{
	IProducesEnergy_ source;
	IRequiresEnergy_ consumer;

	int distance;
	float distanceEnergyDecay;
	float maxEnergyTransfer;

	public EnergyConnection_(IProducesEnergy_ producer, IRequiresEnergy_ requirer,
							 int distance, float decay, float maxTransfer)
	{
		source = producer;
		consumer = requirer;
		this.distance = distance;
		distanceEnergyDecay = decay;
		maxEnergyTransfer = maxTransfer;
	}

	public EnergyConnection_()
	{
	}

	@Nullable
	public static EnergyConnection_ FromNBT(CompoundTag tag, Level level)
	{
		EnergyConnection_ en = new EnergyConnection_();
		DimLocation loc1 = NBTHelper.GetLocationTag(tag, "sourceLoc");

		BlockEntity te1 = null;
		IProducesEnergy_ source = null;

		if (loc1 != null)
		{
			te1 = level.getBlockEntity(loc1.getPos());
		}

		if (te1 != null && te1 instanceof IProducesEnergy_)
		{
			source = (IProducesEnergy_) te1;
		}

		en.source = source;

		DimLocation loc2 = NBTHelper.GetLocationTag(tag, "sourceLoc");

		BlockEntity te2 = null;
		IRequiresEnergy_ consumer = null;

		if (loc2 != null)
		{
			te2 = level.getBlockEntity(loc2.getPos());
		}

		if (te2 != null && te2 instanceof IRequiresEnergy_)
		{
			consumer = (IRequiresEnergy_) te2;
		}

		en.consumer = consumer;

		en.distance = tag.getInt("distance");
		en.distanceEnergyDecay = tag.getFloat("distanceDecay");
		en.maxEnergyTransfer = tag.getFloat("maxTransfer");

		return en;
	}

	public CompoundTag ToNBT()
	{
		CompoundTag tag = new CompoundTag();
		NBTHelper.SetLocationTag(tag, "sourceLoc",
				/*source.GetTe().getLevel().dimensionType()*/0,
				source.GetTe().getBlockPos().getX(),
				source.GetTe().getBlockPos().getY(),
				source.GetTe().getBlockPos().getX());
		NBTHelper.SetLocationTag(tag, "consumerLoc",
				0,
				consumer.GetTe().getBlockPos().getX(),
				consumer.GetTe().getBlockPos().getY(),
				consumer.GetTe().getBlockPos().getX());
		tag.putInt("distance", distance);
		tag.putFloat("distanceDecay", distanceEnergyDecay);
		tag.putFloat("maxTransfer", maxEnergyTransfer);
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
		if (!(ob instanceof EnergyConnection_))
			return false;
		EnergyConnection_ obj = (EnergyConnection_) ob;
		return obj.consumer.equals(consumer) && obj.source.equals(source)
					   && obj.distanceEnergyDecay == distanceEnergyDecay
					   && obj.distance == distance
					   && obj.maxEnergyTransfer == maxEnergyTransfer;
	}
}
