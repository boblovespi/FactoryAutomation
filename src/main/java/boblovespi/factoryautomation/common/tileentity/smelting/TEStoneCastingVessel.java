package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.MOLD;

/**
 * Created by Willi on 12/29/2018.
 */
public class TEStoneCastingVessel extends TileEntity implements ITickable, ICastingVessel
{
	private boolean hasSand;
	private TEStoneCrucible.MetalForms form;
	private ItemStackHandler slot;
	private float temp = 20f;
	private int counter = 0;

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

	@Override
	public TEStoneCrucible.MetalForms GetForm()
	{
		return form;
	}

	@Override
	public void CastInto(ItemStack stack, int temp)
	{
		slot.setStackInSlot(0, stack.copy());
		this.temp = temp;

		/* IMPORTANT */
		markDirty();
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 7);
	}

	@Override
	public float GetLoss()
	{
		return 1.5f;
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
			if (temp < 40f)
			{
				ItemStack taken = TakeItem();
				ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
				if (hasSand)
				{
					ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FABlocks.greenSand.ToBlock()), world);
					SetForm(CastingVesselStates.EMPTY);
				}
			} else
			{
				player.attackEntityFrom(DamageSource.GENERIC, (temp - 40f) / (temp + 100f) * 20f);
				player.sendStatusMessage(
						new TextComponentString("Too hot: " + String.format("%1$.1f\u00b0C", temp)), true);
			}
		} else if (item.getItem() == Item.getItemFromBlock(FABlocks.greenSand.ToBlock())
				&& world.getBlockState(pos).getValue(MOLD) == CastingVesselStates.EMPTY)
		{
			item.shrink(1);
			SetForm(CastingVesselStates.SAND);

			markDirty();
			/* IMPORTANT */
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 7);
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

	@Override
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
		temp = tag.getFloat("temp");
		form = TEStoneCrucible.MetalForms.values()[tag.getInteger("form")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setTag("slot", slot.serializeNBT());
		tag.setBoolean("hasSand", hasSand);
		tag.setFloat("temp", temp);
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

	public ItemStack GetRenderStack()
	{
		return slot.getStackInSlot(0);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if (temp > 20f)
		{
			if (world.isRaining())
				temp *= 0.9938f;
			else
				temp *= 0.9972f;
		}
		counter--;
		if (counter < 0)
		{
			markDirty();
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 7);
			counter = 10;
		}
	}

	public float GetTemp()
	{
		return temp;
	}
}
