package boblovespi.factoryautomation.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;

/**
 * Created by Willi on 12/30/2018.
 */
public class ContainerStoneCastingVessel extends Container
{

	public static final ContainerType<ContainerStoneCastingVessel> TYPE = IForgeContainerType
			.create(ContainerStoneCastingVessel::new);
	private final BlockPos pos;
	private IIntArray form;

	// server-side constructor
	public ContainerStoneCastingVessel(int id, PlayerInventory playerInv, BlockPos pos, IIntArray form)
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
	public ContainerStoneCastingVessel(int id, PlayerInventory playerInv, PacketBuffer extraData)
	{
		this(id, playerInv, extraData.readBlockPos(), new IntArray(1));
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(PlayerEntity playerIn)
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
