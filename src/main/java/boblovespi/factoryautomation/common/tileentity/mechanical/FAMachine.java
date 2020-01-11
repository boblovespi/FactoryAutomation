package boblovespi.factoryautomation.common.tileentity.mechanical;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Willi on 2/18/2018.
 */
public abstract class FAMachine extends TileEntity implements ITickable, ISidedInventory
{
	protected ItemStackHandler inventory;
	protected Map<Direction, int[]> inputs = new HashMap<>(6);
	;
	protected Map<Direction, int[]> outputs = new HashMap<>(6);
	;
	private String name;

	public FAMachine(String name, ItemStackHandler inventory)
	{
		this.name = name;
		this.inventory = inventory;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		return ArrayUtils.addAll(inputs.get(side), outputs.get(side));
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side.
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction side)
	{
		return ArrayUtils.contains(inputs.get(side), index);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side.
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction side)
	{
		return ArrayUtils.contains(outputs.get(side), index);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return inventory.getSlots();
	}

	@Override
	public boolean isEmpty()
	{
		for (int i = 0; i < inventory.getSlots(); ++i)
		{
			if (!inventory.getStackInSlot(i).isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.extractItem(index, count, false);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.extractItem(index, 64, false);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.extractItem(index, 64, false);
		inventory.insertItem(index, stack, false);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override

	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return this.world.getTileEntity(this.pos) == this &&
				player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
						(double) this.pos.getZ() + 0.5D) <= 64.0D;

	}

	@Override
	public void openInventory(PlayerEntity player)
	{
	}

	@Override
	public void closeInventory(PlayerEntity player)
	{
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		//noinspection MethodCallSideOnly
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getTileData()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		readFromNBT(tag);
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("itemHandler"));
		ReadCustomNBT(tag);
	}

	protected abstract void ReadCustomNBT(CompoundNBT tag);

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		super.writeToNBT(tag);
		tag.setTag("itemHandler", inventory.serializeNBT());
		WriteCustomNBT(tag);
		return tag;
	}

	protected abstract void WriteCustomNBT(CompoundNBT tag);

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (facing == null)
				return false;
			if (inputs.get(facing).length == 0)
				return false;
			else
				return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (facing == null)
				return null;
			if (inputs.get(facing).length == 0)
				return null;
			else
				//noinspection unchecked
				return (T) new SidedInvWrapper(this, facing);
		}
		return super.getCapability(capability, facing);
	}
}
