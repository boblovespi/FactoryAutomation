package boblovespi.factoryautomation.api.energy.electricity;

/*
 * Created by Willi on 4/12/2017.
 * a class to store the energy network
*/

import boblovespi.factoryautomation.api.IUpdatable;
import boblovespi.factoryautomation.common.handler.WorldTickHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class EnergyNetwork_ extends WorldSavedData implements IUpdatable
{
	private static final int maxGridSize = 256;
	private static final String DATA_NAME = MODID + "_EnergyNetwork_";
	private static boolean isLoaded = false;
	private List<EnergyConnection_> connections;
	private CompoundNBT uninitData;
	private boolean isInit = false;

	public EnergyNetwork_()
	{
		super(DATA_NAME);
		connections = new ArrayList<>(maxGridSize);
	}

	public static EnergyNetwork_ GetFromWorld(ServerWorld world)
	{
		System.out.println("Loading energy network!");

		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		EnergyNetwork_ instance;
		DimensionSavedDataManager storage = world.getSavedData();
		try
		{
			instance = storage.getOrCreate(EnergyNetwork_::new, DATA_NAME);
		} catch (Exception e)
		{
			instance = null;
		}

		if (instance == null)
		{
			instance = new EnergyNetwork_();
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

	@Override
	public void read(CompoundNBT nbt)
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
	public CompoundNBT write(CompoundNBT nbt)
	{
		for (int i = 0; i < connections.size(); i++)
		{
			nbt.put(String.valueOf(i), connections.get(i).ToNBT());
		}
		return nbt;
	}

	public void Update(World world)
	{
		if (!isInit)
			Init(world);

		connections.forEach(EnergyConnection_::Update);
		// System.out.println("updating!");
		markDirty();
	}

	private void Init(World world)
	{
		if (uninitData != null)
		{
			for (int i = 0; i < uninitData.size(); i++)
			{
				connections.add(EnergyConnection_.FromNBT(uninitData.getCompound(String.valueOf(i)), world));
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
			markDirty();
		}
	}
}

