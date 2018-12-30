package boblovespi.factoryautomation.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Willi on 6/9/2018.
 */
public class PacketHandler
{
	public static SimpleNetworkWrapper INSTANCE = null;
	private static int packetId = 0;

	private static int NextId()
	{
		return packetId++;
	}

	public static void CreateChannel(String channelName)
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		RegisterMessages();
	}

	private static void RegisterMessages()
	{
		// Register messages which are sent from the client to the server here:
		INSTANCE.registerMessage(
				BasicCircuitCreatorSyncPacket.Handler.class, BasicCircuitCreatorSyncPacket.class, NextId(),
				Side.SERVER);
		INSTANCE.registerMessage(
				StoneCastingVesselMoldPacket.Handler.class, StoneCastingVesselMoldPacket.class, NextId(), Side.SERVER);
	}
}
