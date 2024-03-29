package boblovespi.factoryautomation.test;

import boblovespi.factoryautomation.common.container.StringIntArray;
import net.minecraft.world.inventory.DataSlot;

import java.util.ArrayList;
import java.util.List;

public class StringIntArrayTest
{
	private static String cache;
	public static void main(String[] args)
	{
		StringIntArray server = new StringIntArray(10);
		StringIntArray client = new StringIntArray(10);
		server.SetSource(() -> StringIntArrayTest.cache);
		List<DataSlot> serverHolders = new ArrayList<>();
		List<DataSlot> clientHolders = new ArrayList<>();
		for (int i = 0; i < 10; i++)
		{
			serverHolders.add(DataSlot.forContainer(server, i));
			clientHolders.add(DataSlot.forContainer(client, i));
		}

		for (int j = 0; j < 100; j++)
		{
			Random();
			System.out.println(server.GetString());
			for (int i = 0; i < 10; i++)
			{
				if (serverHolders.get(i).checkAndClearUpdateFlag())
					SendToClient(clientHolders, i, serverHolders.get(i).get());
			}
			System.out.println(client.GetString());
		}
	}

	private static void SendToClient(List<DataSlot> client, int prop, int val)
	{
		client.get(prop).set(val);
	}

	private static void Random()
	{
		cache = String.valueOf(Math.floor(Math.exp(Math.random() * 20)));
	}
}
