package boblovespi.factoryautomation.common.network;

import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.smelting.TEStoneCastingVessel;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created by Willi on 12/30/2018.
 */
public class StoneCastingVesselMoldPacket
{
	private BlockPos pos;
	private byte switchTo;

	public StoneCastingVesselMoldPacket(BlockPos pos, byte switchTo)
	{
		this.pos = pos;
		this.switchTo = switchTo;
	}

	public StoneCastingVesselMoldPacket()
	{
		pos = new BlockPos(0, 0, 0);
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 */
	public static StoneCastingVesselMoldPacket FromBytes(ByteBuf buf)
	{
		StoneCastingVesselMoldPacket packet = new StoneCastingVesselMoldPacket();
		packet.pos = BlockPos.of(buf.readLong());
		packet.switchTo = buf.readByte();
		return packet;
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 */
	public void ToBytes(ByteBuf buf)
	{
		buf.writeLong(pos.asLong());
		buf.writeByte(switchTo);
	}

	public void OnMessage(Supplier<NetworkEvent.Context> ctx)
	{
		if (switchTo < 6 && switchTo >= 0)
		{
			ctx.get().enqueueWork(() -> {
				ServerPlayer player = ctx.get().getSender();
				ServerLevel world = Objects.requireNonNull(player).getLevel();
				if (world.hasChunkAt(pos))
				{
					BlockEntity te = world.getBlockEntity(pos);
					if (te instanceof TEStoneCastingVessel
							&& player.containerMenu instanceof ContainerStoneCastingVessel
							&& ((ContainerStoneCastingVessel) player.containerMenu).GetPos().equals(pos)
							&& ((TEStoneCastingVessel) te).HasSand())
					{
						((TEStoneCastingVessel) te)
								.SetForm(StoneCastingVessel.CastingVesselStates.values()[switchTo + 2]);
					}
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
