package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.tileentity.processing.TEStoneCrucible.MetalForms;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.MOLD;

/**
 * Created by Willi on 12/29/2018.
 */
public class TEStoneCastingVessel extends TileEntity
{
	private boolean hasSand;
	private MetalForms form;
	private ItemStackHandler slot;
	private int temp;

	public TEStoneCastingVessel()
	{
		slot = new ItemStackHandler(1);
	}

	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad()
	{
		form = world.getBlockState(pos).getValue(MOLD).metalForm;
	}

	public MetalForms GetForm()
	{
		return form;
	}

	public void CastInto(ItemStack stack, int temp)
	{
		slot.setStackInSlot(0, stack.copy());
		this.temp = temp;
	}

	public void DropItems()
	{
		if (!world.isRemote && !slot.getStackInSlot(0).isEmpty())
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		markDirty();

		/* IMPORTANT */
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, EntityPlayer player)
	{
		if (!slot.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = TakeItem();
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
			if (hasSand)
			{
				ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FABlocks.greenSand.ToBlock()), world);
				SetForm(CastingVesselStates.EMPTY);
			}
		} else if (item.getItem() == Item.getItemFromBlock(FABlocks.greenSand.ToBlock()))
		{
			int itemsTaken = 1 - item.getCount();
			item.shrink(itemsTaken);
			SetForm(CastingVesselStates.SAND);
		}
	}

	public void SetForm(CastingVesselStates state)
	{
		if (state == CastingVesselStates.EMPTY)
			hasSand = false;
		else
			hasSand = true;
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MOLD, state));
		form = state.metalForm;
	}

	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !(oldState.getBlock() == FABlocks.stoneCastingVessel
				&& newState.getBlock() == FABlocks.stoneCastingVessel);
	}

	public boolean HasSpace()
	{
		return slot.getStackInSlot(0).isEmpty();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		slot.deserializeNBT(tag.getCompoundTag("slot"));
		hasSand = tag.getBoolean("hasSand");
		temp = tag.getInteger("temp");
		form = MetalForms.values()[tag.getInteger("form")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setTag("slot", slot.serializeNBT());
		tag.setBoolean("hasSand", hasSand);
		tag.setInteger("temp", temp);
		tag.setInteger("form", form.ordinal());
		return super.writeToNBT(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
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
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	public boolean HasSand()
	{
		return hasSand;
	}
}
