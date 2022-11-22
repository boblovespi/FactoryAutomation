package boblovespi.factoryautomation.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/30/2018.
 */
public class ContainerStoneCastingVessel extends AbstractContainerMenu
{

	public static final MenuType<ContainerStoneCastingVessel> TYPE = IForgeMenuType
			.create(ContainerStoneCastingVessel::new);
	private final BlockPos pos;
	private ContainerData form;

	// server-side constructor
	public ContainerStoneCastingVessel(int id, Inventory playerInv, BlockPos pos, ContainerData form)
	{
		super(TYPE, id);
		this.pos = pos;
		this.form = form;
		addDataSlots(form);
		int x = 8;
		int y = 98;

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
				addSlot(new Slot(playerInv, i + j * 9 + 9, x + i * 18, y + j * 18));
		}
		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(playerInv, i, x + i * 18, y + 58));
		}
	}

	// client-side constructor
	public ContainerStoneCastingVessel(int id, Inventory playerInv, FriendlyByteBuf extraData)
	{
		this(id, playerInv, extraData.readBlockPos(), new SimpleContainerData(1));
	}

	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(Player playerIn)
	{
		return !playerIn.isSpectator();
	}

	public BlockPos GetPos()
	{
		return pos;
	}

	public int GetForm()
	{
		return form.get(0);
	}
}
