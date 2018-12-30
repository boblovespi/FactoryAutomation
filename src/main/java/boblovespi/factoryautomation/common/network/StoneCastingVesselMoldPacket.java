package boblovespi.factoryautomation.common.network;

import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.processing.TEStoneCastingVessel;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Willi on 12/30/2018.
 */
public class StoneCastingVesselMoldPacket implements IMessage
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
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		switchTo = buf.readByte();
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
		buf.writeByte(switchTo);
	}

	public static class Handler implements IMessageHandler<StoneCastingVesselMoldPacket, IMessage>
	{
		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(StoneCastingVesselMoldPacket message, MessageContext ctx)
		{
			if (message.switchTo < 6 && message.switchTo >= 0)
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				WorldServer world = player.getServerWorld();

				world.addScheduledTask(() ->
				{
					if (world.isBlockLoaded(message.pos))
					{
						TileEntity te = world.getTileEntity(message.pos);
						if (te instanceof TEStoneCastingVessel
								&& player.openContainer instanceof ContainerStoneCastingVessel
								&& ((ContainerStoneCastingVessel) player.openContainer).GetPos().equals(message.pos)
								&& ((TEStoneCastingVessel) te).HasSand())
						{
							((TEStoneCastingVessel) te)
									.SetForm(StoneCastingVessel.CastingVesselStates.values()[message.switchTo + 2]);
						}
					}
				});
			}
			return null;
		}
	}
}
