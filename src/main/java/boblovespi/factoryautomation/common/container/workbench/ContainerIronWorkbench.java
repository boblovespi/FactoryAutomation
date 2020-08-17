package boblovespi.factoryautomation.common.container.workbench;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerIronWorkbench extends ContainerWorkbench
{
	public static final ContainerType<ContainerIronWorkbench> TYPE = IForgeContainerType
			.create(ContainerIronWorkbench::new);

	// server-side constructor
	public ContainerIronWorkbench(int id, PlayerInventory playerInv, IItemHandler inv, BlockPos pos)
	{
		super(id, playerInv, inv, pos, true, TYPE);
	}

	// client-side constructor
	public ContainerIronWorkbench(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, new ItemStackHandler(37), extraData.readBlockPos());
	}
}
