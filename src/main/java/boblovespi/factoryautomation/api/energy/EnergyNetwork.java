package boblovespi.factoryautomation.api.energy;

/// *
/// * Created by Willi on 4/12/2017.
/// * <p>
/// * a class to store the energy network

import boblovespi.factoryautomation.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class EnergyNetwork extends WorldSavedData
{
	private static final int maxGridSize = 256;
	private static final String DATA_NAME = MODID + "_EnergyNetwork";
	private List<EnergyConnection> connections;

	public EnergyNetwork()
	{
		super(DATA_NAME);
		connections = new ArrayList<>(maxGridSize);
	}

	public static EnergyNetwork GetFromWorld(World world)
	{
		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		MapStorage storage = world.getPerWorldStorage();
		EnergyNetwork instance = (EnergyNetwork) storage
				.getOrLoadData(EnergyNetwork.class, DATA_NAME);

		if (instance == null)
		{
			instance = new EnergyNetwork();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < nbt.getSize(); i++)
		{
			connections.add(NBTHelper.GetEnergyConnection(
					nbt.getCompoundTag(String.valueOf(i))));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < connections.size(); i++)
		{
			nbt.setTag(String.valueOf(i), connections.get(i).ToNBT());
		}
		return nbt;
	}
}

