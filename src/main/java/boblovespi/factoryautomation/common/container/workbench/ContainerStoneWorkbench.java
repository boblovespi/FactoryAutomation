package boblovespi.factoryautomation.common.container.workbench;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class ContainerStoneWorkbench extends ContainerWorkbench
{
	public static final MenuType<ContainerStoneWorkbench> TYPE = IForgeContainerType
			.create(ContainerStoneWorkbench::new);

	// server-side constructor
	public ContainerStoneWorkbench(int id, Inventory playerInv, IItemHandler inv, BlockPos pos)
	{
		super(id, playerInv, inv, pos, true, TYPE);
	}

	// client-side constructor
	public ContainerStoneWorkbench(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(17), extraData.readBlockPos());
	}
}
