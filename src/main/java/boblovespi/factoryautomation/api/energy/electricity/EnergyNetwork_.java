package boblovespi.factoryautomation.api.energy.electricity;

/*
 * Created by Willi on 4/12/2017.
 * a class to store the energy network
*/

import boblovespi.factoryautomation.api.IUpdatable;
import boblovespi.factoryautomation.common.handler.WorldTickHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class EnergyNetwork_ extends SavedData implements IUpdatable
{
	private static final int maxGridSize = 256;
	private static final String DATA_NAME = MODID + "_EnergyNetwork_";
	private static boolean isLoaded = false;
	private List<EnergyConnection_> connections;
	private CompoundTag uninitData;
	private boolean isInit = false;

	public EnergyNetwork_()
	{
		super(/*DATA_NAME*/);
		connections = new ArrayList<>(maxGridSize);
	}

	public static EnergyNetwork_ GetFromWorld(ServerLevel world)
	{
		System.out.println("Loading energy network!");

		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		EnergyNetwork_ instance;
		DimensionDataStorage storage = world.getDataStorage();
		try
		{
			instance = storage.get(EnergyNetwork_::LoadFromTag, DATA_NAME);
		} catch (Exception e)
		{
			instance = null;
		}

		if (instance == null)
		{
			instance = new EnergyNetwork_();
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

	private static EnergyNetwork_ LoadFromTag(CompoundTag t)
	{
		EnergyNetwork_ e = new EnergyNetwork_();
		e.load(t);
		return e;
	}

	public void load(CompoundTag nbt)
	{
		//		for (int i = 0; i < nbt.getSize(); i++)
		//		{
		//			connections.add(EnergyConnection_.FromNBT(
		//					nbt.getCompoundTag(String.valueOf(i)), ));
		//		}
		uninitData = nbt;
		isInit = false;
	}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		for (int i = 0; i < connections.size(); i++)
		{
			nbt.put(String.valueOf(i), connections.get(i).ToNBT());
		}
		return nbt;
	}

	public void Update(Level level)
	{
		if (!isInit)
			Init(level);

		connections.forEach(EnergyConnection_::Update);
		// System.out.println("updating!");
		setDirty();
	}

	private void Init(Level level)
	{
		if (uninitData != null)
		{
			for (int i = 0; i < uninitData.size(); i++)
			{
				connections.add(EnergyConnection_.FromNBT(uninitData.getCompound(String.valueOf(i)), level));
			}
		}
		isInit = true;
	}

	public void AddConnection(EnergyConnection_ connection)
	{
		if (!connections.contains(connection))
		{
			connections.add(connection);
			connection.consumer.AddConnection(connection);
			connection.source.AddConnection(connection);
			setDirty();
		}
	}
}

