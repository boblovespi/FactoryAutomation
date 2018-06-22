package boblovespi.factoryautomation.common.network;

import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Willi on 6/9/2018.
 */
public class BasicCircuitCreatorSyncPacket implements IMessage
{
	private BlockPos te;
	private byte mode;
	private byte gridX;
	private byte gridY;

	public BasicCircuitCreatorSyncPacket()
	{
		te = null;
	}

	public BasicCircuitCreatorSyncPacket(BlockPos te, byte mode, byte gridX, byte gridY)
	{
		this.te = te;
		this.mode = mode;
		this.gridX = gridX;
		this.gridY = gridY;
	}

	/**
	 * Convert from the supplied buffer into your specific message type
	 */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		te = BlockPos.fromLong(buf.readLong());
		mode = buf.readByte();
		gridX = buf.readByte();
		gridY = buf.readByte();
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(te.toLong());
		buf.writeByte(mode);
		buf.writeByte(gridX);
		buf.writeByte(gridY);
	}

	public static class Handler implements IMessageHandler<BasicCircuitCreatorSyncPacket, IMessage>
	{

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(BasicCircuitCreatorSyncPacket message, MessageContext ctx)
		{
			System.out.println("message received!");
			System.out.println("message.te = " + message.te);
			System.out.println("message.mode = " + message.mode);
			System.out.println("message.gridX = " + message.gridX);
			System.out.println("message.gridY = " + message.gridY);

			EntityPlayerMP player = ctx.getServerHandler().player;
			WorldServer world = player.getServerWorld();

			world.addScheduledTask(() ->
			{
				if (world.isBlockLoaded(message.te))
				{
					TileEntity te = world.getTileEntity(message.te);
					if (te instanceof TEBasicCircuitCreator
							&& player.openContainer instanceof ContainerBasicCircuitCreator
							&& ((ContainerBasicCircuitCreator) player.openContainer).GetPos().equals(message.te))
					{
						TEBasicCircuitCreator teC = (TEBasicCircuitCreator) te;
						int x = message.gridX;
						int y = message.gridY;
						switch (message.mode)
						{
						case 3:
							teC.RemoveComponent(x, y);
							break;
						case 0:
							teC.AddComponent(TEBasicCircuitCreator.Layout.Element.SOLDER, x, y);
							break;
						case 1:
							teC.AddComponent(TEBasicCircuitCreator.Layout.Element.WIRE, x, y);
							break;
						case 2:
							IItemHandler inv = teC.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
							if (inv != null)
							{
								ItemStack stack = inv.getStackInSlot(3).copy();
								if (stack.getItem() == FAItems.basicChip)
									teC.AddComponent(TEBasicCircuitCreator.Layout.Element.CHIP, x, y);
								else if (false)
									;
							}

							break;
						case 4:
							teC.Craft();
							break;
						}
					}

				}
			});

			return null;
		}
	}
}
