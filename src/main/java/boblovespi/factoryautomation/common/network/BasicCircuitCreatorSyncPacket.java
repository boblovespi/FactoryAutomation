package boblovespi.factoryautomation.common.network;

import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TEBasicCircuitCreator;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Supplier;

/**
 * Created by Willi on 6/9/2018.
 */
public class BasicCircuitCreatorSyncPacket
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
	public static BasicCircuitCreatorSyncPacket FromBytes(ByteBuf buf)
	{
		BasicCircuitCreatorSyncPacket packet = new BasicCircuitCreatorSyncPacket();
		packet.te = BlockPos.fromLong(buf.readLong());
		packet.mode = buf.readByte();
		packet.gridX = buf.readByte();
		packet.gridY = buf.readByte();
		return packet;
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 */
	public void ToBytes(ByteBuf buf)
	{
		buf.writeLong(te.toLong());
		buf.writeByte(mode);
		buf.writeByte(gridX);
		buf.writeByte(gridY);
	}

	public void OnMessage(Supplier<NetworkEvent.Context> ctx)
	{
		System.out.println("message received!");
		System.out.println("message.te = " + te);
		System.out.println("message.mode = " + mode);
		System.out.println("message.gridX = " + gridX);
		System.out.println("message.gridY = " + gridY);

		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getServerWorld();

			if (world.isBlockLoaded(te))
			{
				TileEntity te = world.getTileEntity(this.te);
				if (te instanceof TEBasicCircuitCreator
						&& player.openContainer instanceof ContainerBasicCircuitCreator
						&& ((ContainerBasicCircuitCreator) player.openContainer).GetPos().equals(this.te))
				{
					TEBasicCircuitCreator teC = (TEBasicCircuitCreator) te;
					int x = gridX;
					int y = gridY;
					switch (mode)
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
						IItemHandler inv = teC.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
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
		ctx.get().setPacketHandled(true);
	}
}
