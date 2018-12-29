package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.CampfireRecipe;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.Campfire;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/27/2018.
 */
public class TECampfire extends TileEntity implements ITickable
{
	private ItemStackHandler slot;
	private String recipe = "none";
	private int timeLeft = -1;
	private boolean isLit = false;

	public TECampfire()
	{
		slot = new ItemStackHandler(1);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote)
			return;
		if ("none".equals(recipe))
			return;
		if (!isLit)
			return;
		timeLeft--;
		if (timeLeft < 0)
		{
			CampfireRecipe getRecipe = CampfireRecipe.GetRecipe(recipe);
			ItemStack output = getRecipe.GetOutput();
			slot.setStackInSlot(0, output.copy());
			recipe = "none";
			timeLeft = -1;

			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
		markDirty();
	}

	public void DropItems()
	{
		if (!world.isRemote && !slot.getStackInSlot(0).isEmpty())
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack PlaceItem(ItemStack items)
	{
		if (!slot.getStackInSlot(0).isEmpty())
			return items;
		ItemStack stack = slot.insertItem(0, items, false);
		CampfireRecipe getRecipe = CampfireRecipe.FindRecipe(slot.getStackInSlot(0));
		if (getRecipe != null)
		{
			recipe = getRecipe.GetName();
			timeLeft = getRecipe.GetTime();
			isLit = world.getBlockState(pos).getValue(Campfire.LIT);
		} else
		{
			recipe = "none";
			timeLeft = -1;
		}
		markDirty();

		/* IMPORTANT */
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 7);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		timeLeft = -1;
		recipe = "none";
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
		} else
		{
			ItemStack stack = PlaceItem(item.copy().splitStack(1));
			int itemsTaken = 1 - stack.getCount();
			item.shrink(itemsTaken);
		}
	}

	public ItemStack GetRenderStack()
	{
		return slot.getStackInSlot(0);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		timeLeft = tag.getInteger("timeLeft");
		slot.deserializeNBT(tag.getCompoundTag("slot"));
		recipe = tag.getString("recipe");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("timeLeft", timeLeft);
		tag.setString("recipe", recipe);
		tag.setTag("slot", slot.serializeNBT());
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

	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !(oldState.getBlock() == FABlocks.campfire && newState.getBlock() == FABlocks.campfire);
	}

	public void SetLit(boolean isLit)
	{
		this.isLit = isLit;
	}
}
