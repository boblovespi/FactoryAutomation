package boblovespi.factoryautomation.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 6/9/2018.
 */
public class PacketHandler
{
	public static String PROTOCOL_VERSION = "1.0";
	public static SimpleChannel INSTANCE = null;
	private static int packetId = 0;

	private static int NextId()
	{
		return packetId++;
	}

	public static void CreateChannel(String channelName)
	{
		if (INSTANCE != null)
			return;
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, channelName), () -> PROTOCOL_VERSION,
													PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
		RegisterMessages();
	}

	private static void RegisterMessages()
	{
		// Register messages which are sent from the client to the server here:
		INSTANCE.registerMessage(NextId(), BasicCircuitCreatorSyncPacket.class, BasicCircuitCreatorSyncPacket::ToBytes,
				BasicCircuitCreatorSyncPacket::FromBytes, BasicCircuitCreatorSyncPacket::OnMessage,
				Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(NextId(), StoneCastingVesselMoldPacket.class, StoneCastingVesselMoldPacket::ToBytes,
				StoneCastingVesselMoldPacket::FromBytes, StoneCastingVesselMoldPacket::OnMessage,
				Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
}
