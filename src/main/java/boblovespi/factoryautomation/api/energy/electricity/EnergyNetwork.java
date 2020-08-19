package boblovespi.factoryautomation.api.energy.electricity;

import boblovespi.factoryautomation.api.IUpdatable;
import boblovespi.factoryautomation.api.energy.electricity.enums.WireType;
import boblovespi.factoryautomation.common.handler.WorldTickHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 7/12/2018.
 */
public class EnergyNetwork extends WorldSavedData implements IUpdatable
{
	private static final int defaultGridSize = 256;
	private static final String DATA_NAME = MODID + "_EnergyNetworkNew";
	private static boolean isLoaded = false;
	private CompoundNBT uninitData = null;
	private boolean isInit = false;

	private Map<IProducesEnergy, List<EnergyConnection>> connections = new HashMap<>(defaultGridSize);

	public EnergyNetwork()
	{
		super(DATA_NAME);
	}

	public EnergyNetwork(String unused)
	{
		super(DATA_NAME);
	}

	public static EnergyNetwork GetFromWorld(ServerWorld world)
	{
		System.out.println("Loading energy network!");

		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		EnergyNetwork instance;
		DimensionSavedDataManager storage = world.getSavedData();
		try
		{
			instance = (EnergyNetwork) storage.getOrCreate(EnergyNetwork::new, DATA_NAME);
		} catch (Exception e)
		{
			instance = null;
		}

		if (instance == null)
		{
			instance = new EnergyNetwork();
			storage.set(instance);
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

	private void Init(World world)
	{
		if (isInit)
			return;
		isInit = true;
		if (uninitData == null)
			return;

		ListNBT list = uninitData.getList("connections", 10);
		Iterator<INBT> iterator = list.iterator();
		while (iterator.hasNext())
		{
			CompoundNBT next = (CompoundNBT) iterator.next();
			EnergyConnection e = ReadConnectionFromTag(world, next);
			AddConnection(e);
		}

	}

	@Override
	public void Update(World world)
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
	@Override
	public void read(CompoundNBT tag)
	{
		isInit = false;
		uninitData = tag;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		ListNBT list = new ListNBT();

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
		markDirty();
	}

	private CompoundNBT WriteConnectionToTag(IProducesEnergy p, EnergyConnection e)
	{
		CompoundNBT tag = new CompoundNBT();

		tag.put("producerPos", NBTUtil.writeBlockPos(p.GetTe().getPos()));
		CompoundNBT eTag = new CompoundNBT();

		eTag.put("consumerPos", NBTUtil.writeBlockPos(e.GetConsumer().GetTe().getPos()));
		eTag.putDouble("maxVoltage", e.maxVoltage);
		eTag.putDouble("maxAmperage", e.maxAmperage);
		eTag.putInt("wireType", e.wire.ordinal());
		eTag.putDouble("wireLength", e.wireLength);

		tag.put("energyConnection", eTag);

		return tag;
	}

	private EnergyConnection ReadConnectionFromTag(World w, CompoundNBT tag)
	{
		BlockPos pPos = NBTUtil.readBlockPos(tag.getCompound("producerPos"));
		CompoundNBT tag1 = tag.getCompound("energyConnection");
		BlockPos cPos = NBTUtil.readBlockPos(tag1.getCompound("consumerPos"));

		double maxVoltage = tag1.getDouble("maxVoltage");
		double maxAmperage = tag1.getDouble("maxAmperage");
		int wireType = tag1.getInt("wireType");
		double wireLength = tag1.getDouble("wireLength");

		TileEntity pTE = w.getTileEntity(pPos);
		TileEntity cTE = w.getTileEntity(cPos);

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