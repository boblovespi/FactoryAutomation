package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.machine.BlastFurnaceController;
import boblovespi.factoryautomation.common.container.ContainerBlastFurnace;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.util.RestrictedSlotItemHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
@MethodsReturnNonnullByDefault
public class TEBlastFurnaceController extends TileEntity
		implements ITickableTileEntity, IMultiblockControllerTE, INamedContainerProvider
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
	private final IIntArray containerInfo = new IIntArray()
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
	public void load(BlockState state, CompoundNBT compound)
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
	public CompoundNBT save(CompoundNBT compound)
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
		if (Objects.requireNonNull(world).isClientSide)
			return;

		if (!world.getBlockState(levelPosition).getValue(BlastFurnaceController.MULTIBLOCK_COMPLETE))
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
					itemHandler.insertItem(SLAG_SLOT, new ItemStack(FAItems.slag.toItem(), 1), false);
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
		BlockState state = world.getBlockState(levelPosition);
		world.sendBlockUpdated(levelPosition, state, state, 3);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(Objects.requireNonNull(world).getBlockState(levelPosition), pkt.getTag());
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
	public void setStructureValid(boolean isValid)
	{
		isStructureValid = isValid;
		Block block = Objects.requireNonNull(world).getBlockState(levelPosition).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).setStructureCompleted(world, levelPosition, isValid);
		}
	}

	@Override
	public boolean isStructureValid()
	{
		return isStructureValid;
	}

	@Override
	public void createStructure()
	{
		Block block = Objects.requireNonNull(world).getBlockState(levelPosition).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).createStructure(world, levelPosition);
		}
	}

	@Override
	public void breakStructure()
	{
		Block block = getBlockState().getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).breakStructure(world, levelPosition);
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
	public <T> LazyOptional<T> getCapability(Capability<T> capability, int[] offset, Direction side)
	{
		if (offset[0] == 1 && offset[1] == 4 && offset[2] == 1 && side == Direction.UP
				&& capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) inputHopperWrapper);
		return null;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerBlastFurnace(id, playerInv, itemHandler, containerInfo, levelPosition);
	}
}
