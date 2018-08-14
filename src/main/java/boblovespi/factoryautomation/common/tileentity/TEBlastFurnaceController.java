package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.block.machine.BlastFurnaceController;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.util.RestrictedSlotItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Willi on 11/12/2017.
 * shamelessly copied over from my old mod
 */
public class TEBlastFurnaceController extends TileEntity
		implements ITickable, ICapabilityProvider, IMultiblockControllerTE
{
	public static final int[] COKE_SLOTS = { 3, 4 };
	public static final int IRON_SLOT = 1;
	public static final int FLUX_SLOT = 2;
	public static final int OUTPUT_SLOT = 5;
	public static final int SLAG_SLOT = 6;
	public static final int TUYERE_SLOT = 0;
	private ItemStackHandler itemHandler;
	private boolean isBurningFuel = false; // whether the blast furnace is burning fuel or not
	private boolean isSmeltingItem = false; // whether the blast furnace is smelting or not

	private float burnTime = 0; // the remaining burn time of the blast furnace
	private float fuelBurnTime = 200; // the amount of time a new piece of the same fuel would burn

	private float smeltTime; // the remaining time for the ingot to smelt to pig iron
	private float steelSmeltTime; // the amount of time it takes to smelt pig iron

	private float burnScalar = 2; // the speed at which the coal coke burns
	private float smeltScalar = 1; // the speed at which the pig iron smelts
	private boolean isStructureValid = false;
	private IItemHandler inputHopperWrapper;

	public TEBlastFurnaceController()
	{
		itemHandler = new ItemStackHandler(7);
		inputHopperWrapper = new RestrictedSlotItemHandler(new HashSet<Integer>()
		{{
			add(0);
			add(5);
			add(6);
		}}, itemHandler);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		burnTime = compound.getInteger("burnTime");
		fuelBurnTime = compound.getInteger("fuelBurnTime");
		isBurningFuel = compound.getBoolean("isBurningFuel");

		smeltTime = compound.getInteger("smeltTime");
		steelSmeltTime = compound.getInteger("steelSmeltTime");
		isSmeltingItem = compound.getBoolean("isSmeltingItem");

		itemHandler.deserializeNBT(compound.getCompoundTag("itemHandler"));

		super.readFromNBT(compound);

		//		inputHopperWrapper = new RestrictedSlotItemHandler(new HashSet<Integer>()
		//		{{
		//			add(0);
		//			add(5);
		//			add(6);
		//		}}, itemHandler);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("burnTime", (int) burnTime);
		compound.setInteger("fuelBurnTime", (int) fuelBurnTime);
		compound.setBoolean("isBurningFuel", isBurningFuel);

		compound.setInteger("smeltTime", (int) smeltTime);
		compound.setInteger("steelSmeltTime", (int) steelSmeltTime);
		compound.setBoolean("isSmeltingItem", isSmeltingItem);

		compound.setTag("itemHandler", itemHandler.serializeNBT());

		return super.writeToNBT(compound);
	}

	@Override
	public void update()
	{
		if (world.isRemote)
			return;

		if (!world.getBlockState(pos).getValue(BlastFurnaceController.MULTIBLOCK_COMPLETE))
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
					itemHandler.insertItem(SLAG_SLOT, new ItemStack(FAItems.slag.ToItem(), 1, 0), false);
				}
			} else
			{
				if (!itemHandler.getStackInSlot(COKE_SLOTS[0]).isEmpty())
				{
					fuelBurnTime = itemHandler.getStackInSlot(COKE_SLOTS[0]).getItem()
											  .getItemBurnTime(itemHandler.getStackInSlot(COKE_SLOTS[0]));

					itemHandler.extractItem(COKE_SLOTS[0], 1, false);
					burnTime = fuelBurnTime;
					isBurningFuel = true;
				} else if (!itemHandler.getStackInSlot(COKE_SLOTS[1]).isEmpty())
				{
					fuelBurnTime = itemHandler.getStackInSlot(COKE_SLOTS[0]).getItem()
											  .getItemBurnTime(itemHandler.getStackInSlot(COKE_SLOTS[0]));

					itemHandler.extractItem(COKE_SLOTS[1], 1, false);
					burnTime = fuelBurnTime;
					isBurningFuel = true;
				} else
				{
					smeltTime = smeltTime + 10 * smeltScalar > steelSmeltTime ? steelSmeltTime :
							smeltTime + 10 * smeltScalar;
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
		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) itemHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
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
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).SetStructureCompleted(world, pos, isValid);
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
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).CreateStructure(world, pos);
		}
	}

	@Override
	public void BreakStructure()
	{
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlastFurnaceController)
		{
			((BlastFurnaceController) block).BreakStructure(world, pos);
		}
	}

	/**
	 * Gets the capability, or null, of the block at offset for the given side
	 *
	 * @param capability the type of capability to get
	 * @param offset     the offset of the multiblock part
	 * @param side       the side which is accessed
	 * @return the capability implementation which to use
	 */
	@Override
	public <T> T GetCapability(Capability<T> capability, int[] offset, EnumFacing side)
	{
		if (offset[0] == 1 && offset[1] == 4 && offset[2] == 1 && side == EnumFacing.UP
				&& capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inputHopperWrapper;
		return null;
	}
}
