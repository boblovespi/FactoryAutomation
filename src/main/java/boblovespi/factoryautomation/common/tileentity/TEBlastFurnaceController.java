package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.machine.BlastFurnaceController;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.util.RestrictedSlotItemHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.BitSet;
import java.util.Objects;

/**
 * Created by Willi on 11/12/2017.
 * shamelessly copied over from my old mod
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
public class TEBlastFurnaceController extends BlockEntity
		implements TickableBlockEntity, IMultiblockControllerTE, MenuProvider
{
	public static final int[] COKE_SLOTS = { 3, 4 };
	public static final int IRON_SLOT = 1;
	public static final int FLUX_SLOT = 2;
	public static final int OUTPUT_SLOT = 5;
	public static final int SLAG_SLOT = 6;
	public static final int TUYERE_SLOT = 0;
	private final ItemStackHandler itemHandler;
	private boolean isBurningFuel = false; // whether the blast furnace is burning fuel or not
	private boolean isSmeltingItem = false; // whether the blast furnace is smelting or not

	private float burnTime = 0; // the remaining burn time of the blast furnace
	private float fuelBurnTime = 200; // the amount of time a new piece of the same fuel would burn

	private float smeltTime; // the remaining time for the ingot to smelt to pig iron
	private float steelSmeltTime; // the amount of time it takes to smelt pig iron

	private final float burnScalar = 2; // the speed at which the coal coke burns
	private final float smeltScalar = 1; // the speed at which the pig iron smelts
	private boolean isStructureValid = false;
	private final IItemHandler inputHopperWrapper;
	private final ContainerData containerInfo = new ContainerData()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return (int) (getRemainingBurnTime() / getLastBurnTime() * 14); // fuel burn time draw scalar
			case 1:
				return (int) (getCurrentSmeltTime() / getTotalSmeltTime() * 24); // smelt time draw scalar
			case 2:
				return isBurningFuel() ? 1 : 0; // if it is burning stuff
			case 3:
				return isSmeltingItem() ? 1 : 0; // if it is smelting
			}
			return 0;
		}

		@Override
		public void set(int index, int value)
		{

		}

		@Override
		public int getCount()
		{
			return 4;
		}
	};

	public TEBlastFurnaceController()
	{
		super(TileEntityHandler.teBlastFurnaceController);
		itemHandler = new ItemStackHandler(7);
		inputHopperWrapper = new RestrictedSlotItemHandler(new BitSet(7)
		{{
			set(5);
			set(6);
			set(0);
		}}, itemHandler);
	}

	@Override
	public void load(BlockState state, CompoundTag compound)
	{
		burnTime = compound.getInt("burnTime");
		fuelBurnTime = compound.getInt("fuelBurnTime");
		isBurningFuel = compound.getBoolean("isBurningFuel");

		smeltTime = compound.getInt("smeltTime");
		steelSmeltTime = compound.getInt("steelSmeltTime");
		isSmeltingItem = compound.getBoolean("isSmeltingItem");

		itemHandler.deserializeNBT(compound.getCompound("itemHandler"));

		super.load(state, compound);

		//		inputHopperWrapper = new RestrictedSlotItemHandler(new HashSet<Integer>()
		//		{{
		//			add(0);
		//			add(5);
		//			add(6);
		//		}}, itemHandler);
	}

	@Override
	public CompoundTag save(CompoundTag compound)
	{
		compound.putInt("burnTime", (int) burnTime);
		compound.putInt("fuelBurnTime", (int) fuelBurnTime);
		compound.putBoolean("isBurningFuel", isBurningFuel);

		compound.putInt("smeltTime", (int) smeltTime);
		compound.putInt("steelSmeltTime", (int) steelSmeltTime);
		compound.putBoolean("isSmeltingItem", isSmeltingItem);

		compound.put("itemHandler", itemHandler.serializeNBT());

		return super.save(compound);
	}

	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
			return;

		if (!level.getBlockState(worldPosition).getValue(BlastFurnaceController.MULTIBLOCK_COMPLETE))
			return;

		steelSmeltTime = 2000; // TODO: read from config
		if (isSmeltingItem)
		{
			if (isBurningFuel)
			{
				burnTime -= burnScalar; // TODO: read from config
				smeltTime -= smeltScalar; // TODO: read from config
				if (burnTime <= 0)
				{
					isBurningFuel = false;
				}
				if (smeltTime <= 0)
				{
					isSmeltingItem = false;
					itemHandler.extractItem(IRON_SLOT, 1, false);
					itemHandler.extractItem(FLUX_SLOT, 1, false);
					itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(FAItems.ingot.GetItem(Metals.PIG_IRON)), false);
					itemHandler.insertItem(SLAG_SLOT, new ItemStack(FAItems.slag.ToItem(), 1), false);
				}
			} else
			{
				if (!itemHandler.getStackInSlot(COKE_SLOTS[0]).isEmpty())
				{
					fuelBurnTime = itemHandler.getStackInSlot(COKE_SLOTS[0]).getItem()
											  .getBurnTime(itemHandler.getStackInSlot(COKE_SLOTS[0]));

					itemHandler.extractItem(COKE_SLOTS[0], 1, false);
					burnTime = fuelBurnTime;
					isBurningFuel = true;
				} else if (!itemHandler.getStackInSlot(COKE_SLOTS[1]).isEmpty())
				{
					fuelBurnTime = itemHandler.getStackInSlot(COKE_SLOTS[0]).getItem()
											  .getBurnTime(itemHandler.getStackInSlot(COKE_SLOTS[0]));

					itemHandler.extractItem(COKE_SLOTS[1], 1, false);
					burnTime = fuelBurnTime;
					isBurningFuel = true;
				} else
				{
					smeltTime = Math.min(smeltTime + 10 * smeltScalar, steelSmeltTime);
				}
			}
		} else if (isBurningFuel)
		{
			if (!itemHandler.getStackInSlot(IRON_SLOT).isEmpty() && !itemHandler.getStackInSlot(FLUX_SLOT).isEmpty()
					&& !itemHandler.getStackInSlot(TUYERE_SLOT).isEmpty())
			{
				smeltTime = steelSmeltTime;
				isSmeltingItem = true;
			}
		} else if (!itemHandler.getStackInSlot(IRON_SLOT).isEmpty() && !itemHandler.getStackInSlot(FLUX_SLOT).isEmpty()
				&& !itemHandler.getStackInSlot(TUYERE_SLOT).isEmpty())
		{
			if (!itemHandler.getStackInSlot(COKE_SLOTS[0]).isEmpty())
			{
				itemHandler.extractItem(COKE_SLOTS[0], 1, false);
				burnTime = fuelBurnTime;
				isBurningFuel = true;
				smeltTime = steelSmeltTime;
				isSmeltingItem = true;
			} else if (!itemHandler.getStackInSlot(COKE_SLOTS[1]).isEmpty())
			{
				itemHandler.extractItem(COKE_SLOTS[1], 1, false);
				burnTime = fuelBurnTime;
				isBurningFuel = true;
				smeltTime = steelSmeltTime;
				isSmeltingItem = true;
			}
		}
		setChanged();
		BlockState state = level.getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, 3);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return new ClientboundBlockEntityDataPacket(worldPosition, 0, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		this.load(Objects.requireNonNull(level).getBlockState(worldPosition), pkt.getTag());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) itemHandler);
		return super.getCapability(capability, facing);
	}

	public boolean isBurningFuel()
	{
		return isBurningFuel;
	}

	public float getLastBurnTime()
	{
		return fuelBurnTime;
	}

	public float getRemainingBurnTime()
	{
		return burnTime;
	}

	public float getCurrentSmeltTime()
	{
		return smeltTime;
	}

	public float getTotalSmeltTime()
	{
		return steelSmeltTime;
	}

	public boolean isSmeltingItem()
	{
		return isSmeltingItem;
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		isStructureValid = isValid;
		Block block = Objects.requireNonNull(level).getBlockState(worldPosition).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).SetStructureCompleted(level, worldPosition, isValid);
		}
	}

	@Override
	public boolean IsStructureValid()
	{
		return isStructureValid;
	}

	@Override
	public void CreateStructure()
	{
		Block block = Objects.requireNonNull(level).getBlockState(worldPosition).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).CreateStructure(level, worldPosition);
		}
	}

	@Override
	public void BreakStructure()
	{
		Block block = getBlockState().getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).BreakStructure(level, worldPosition);
		}
	}

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 *
	 * Todo: this method should return a nonnull lazy-optional.
	 *
	 * @param capability the type of capability to get
	 * @param offset     the offset of the multiblock part
	 * @param side       the side which is accessed
	 * @return the capability implementation which to use
	 */
	@Nullable
    @Override
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		if (offset[0] == 1 && offset[1] == 4 && offset[2] == 1 && side == Direction.UP
				&& capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) inputHopperWrapper);
		return null;
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerBlastFurnace(id, playerInv, itemHandler, containerInfo, worldPosition);
	}
}
