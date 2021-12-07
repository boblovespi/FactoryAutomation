package boblovespi.factoryautomation.common.container.workbench;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

public class ContainerIronWorkbench extends ContainerWorkbench
{
	public static final MenuType<ContainerIronWorkbench> TYPE = IForgeMenuType
			.create(ContainerIronWorkbench::new);

	// server-side constructor
	public ContainerIronWorkbench(int id, Inventory playerInv, IItemHandler inv, BlockPos pos)
	{
		super(id, playerInv, inv, pos, false, TYPE);
	}

	// client-side constructor
	public ContainerIronWorkbench(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, new ItemStackHandler(37), extraData.readBlockPos());
	}
}
