package boblovespi.factoryautomation.api.energy.electricity;

import boblovespi.factoryautomation.api.IUpdatable;
import boblovespi.factoryautomation.api.energy.electricity.enums.WireType;
import boblovespi.factoryautomation.common.handler.WorldTickHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 7/12/2018.
 */
public class EnergyNetwork extends SavedData implements IUpdatable
{
	private static final int defaultGridSize = 256;
	private static final String DATA_NAME = MODID + "_EnergyNetworkNew";
	private static boolean isLoaded = false;
	private CompoundTag uninitData = null;
	private boolean isInit = false;

	private Map<IProducesEnergy, List<EnergyConnection>> connections = new HashMap<>(defaultGridSize);

	public EnergyNetwork()
	{
		super(/*DATA_NAME*/);
	}

	public EnergyNetwork(String unused)
	{
		super(/*DATA_NAME*/);
	}

	public static EnergyNetwork GetFromWorld(ServerLevel world)
	{
		System.out.println("Loading energy network!");

		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		EnergyNetwork instance;
		DimensionDataStorage storage = world.getDataStorage();
		try
		{
			instance = (EnergyNetwork) storage.get(EnergyNetwork::LoadFromTag, DATA_NAME);
		} catch (Exception e)
		{
			instance = null;
		}

		if (instance == null)
		{
			instance = new EnergyNetwork();
			storage.set(DATA_NAME, instance);
		}
		if (!isLoaded)
		{
			WorldTickHandler.GetInstance().AddHandler(instance);
			isLoaded = true;
		}
		if (!instance.isInit)
			instance.Init(world);
		return instance;
	}

	private static EnergyNetwork LoadFromTag(CompoundTag t)
	{
		EnergyNetwork e = new EnergyNetwork();
		e.load(t);
		return e;
	}

	private void Init(Level world)
	{
		if (isInit)
			return;
		isInit = true;
		if (uninitData == null)
			return;

		ListTag list = uninitData.getList("connections", 10);
		Iterator<Tag> iterator = list.iterator();
		while (iterator.hasNext())
		{
			CompoundTag next = (CompoundTag) iterator.next();
			EnergyConnection e = ReadConnectionFromTag(world, next);
			AddConnection(e);
		}

	}

	@Override
	public void Update(Level world)
	{
		if (!isInit)
			Init(world);

		for (Map.Entry<IProducesEnergy, List<EnergyConnection>> entry : connections.entrySet())
		{
			IProducesEnergy producer = entry.getKey();
			List<EnergyConnection> list = entry.getValue();

			int listSize = list.size();
			if (producer instanceof IStoresEnergy && !((IStoresEnergy) producer).IsDischarging())
				continue;
			if (listSize == 1)
				list.get(0).UpdateConnection(-1, producer.ActualWattageProduced());
			else if (listSize > 1)
			{
				double[] currents = new double[listSize];
				double currentSum = 0;

				for (int i = 0; i < currents.length; i++)
				{
					double temp = list.get(i).SimulateUpdateConnection(20);
					currents[i] = temp;
					currentSum += temp;
				}

				double ratio = producer.TotalWattageProduced() / (currentSum * 20);

				for (int i = 0; i < listSize; i++)
				{
					EnergyConnection ec = list.get(i);
					ec.GetConsumer().SetVoltageRecieved(20 * ratio);
					ec.GetConsumer().SetAmperageRecieved(currents[i] * ratio);
				}
			}
		}
		// TODO: add logic for many producer -> 1 consumer
	}

	/**
	 * reads in data from the CompoundNBT into this MapDataBase
	 */
	public void load(CompoundTag tag)
	{
		isInit = false;
		uninitData = tag;
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		ListTag list = new ListTag();

		connections.values().forEach(l -> l.forEach(e -> list.add(WriteConnectionToTag(e.GetProducer(), e))));
		tag.put("connections", list);

		return tag;
	}

	public void AddConnection(EnergyConnection e)
	{
		if (e == null)
			return;
		if (connections.containsKey(e.GetProducer()))
		{
			if (!connections.get(e.GetProducer()).contains(e))
				connections.get(e.GetProducer()).add(e);
		} else
		{
			connections.put(e.GetProducer(), new ArrayList<>(8));
			connections.get(e.GetProducer()).add(e);
		}
		setDirty();
	}

	private CompoundTag WriteConnectionToTag(IProducesEnergy p, EnergyConnection e)
	{
		CompoundTag tag = new CompoundTag();

		tag.put("producerPos", NbtUtils.writeBlockPos(p.GetTe().getBlockPos()));
		CompoundTag eTag = new CompoundTag();

		eTag.put("consumerPos", NbtUtils.writeBlockPos(e.GetConsumer().GetTe().getBlockPos()));
		eTag.putDouble("maxVoltage", e.maxVoltage);
		eTag.putDouble("maxAmperage", e.maxAmperage);
		eTag.putInt("wireType", e.wire.ordinal());
		eTag.putDouble("wireLength", e.wireLength);

		tag.put("energyConnection", eTag);

		return tag;
	}

	private EnergyConnection ReadConnectionFromTag(Level w, CompoundTag tag)
	{
		BlockPos pPos = NbtUtils.readBlockPos(tag.getCompound("producerPos"));
		CompoundTag tag1 = tag.getCompound("energyConnection");
		BlockPos cPos = NbtUtils.readBlockPos(tag1.getCompound("consumerPos"));

		double maxVoltage = tag1.getDouble("maxVoltage");
		double maxAmperage = tag1.getDouble("maxAmperage");
		int wireType = tag1.getInt("wireType");
		double wireLength = tag1.getDouble("wireLength");

		BlockEntity pTE = w.getBlockEntity(pPos);
		BlockEntity cTE = w.getBlockEntity(cPos);

		if (pTE instanceof IProducesEnergy)
		{
			if (cTE instanceof IConsumesEnergy)
			{
				return new EnergyConnection((IProducesEnergy) pTE, (IConsumesEnergy) cTE, maxVoltage, maxAmperage,
						WireType.values()[wireType], wireLength);
			}
		}
		return null;
	}
}