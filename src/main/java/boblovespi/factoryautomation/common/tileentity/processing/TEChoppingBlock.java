package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.block.processing.ChoppingBlock;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/26/2018.
 */
public class TEChoppingBlock extends TileEntity
{
	private int maxClicks = 5;
	private int clicksLeft = -1;
	private ItemStackHandler slot;
	private int craftsBeforeBreak;
	private int craftsDone = 0;
	private String recipe = "none";

	public TEChoppingBlock()
	{
		slot = new ItemStackHandler(1);
	}

	@Override
	public void onLoad()
	{
		craftsBeforeBreak = ((ChoppingBlock) world.getBlockState(pos).getBlock()).maxUses;
	}

	public ItemStack PlaceItem(ItemStack items)
	{
		if (!slot.getStackInSlot(0).isEmpty())
			return items;
		ItemStack stack = slot.insertItem(0, items, false);
		ChoppingBlockRecipe getRecipe = ChoppingBlockRecipe.FindRecipe(slot.getStackInSlot(0));
		if (getRecipe != null)
		{
			recipe = getRecipe.GetName();
			clicksLeft = maxClicks;
		} else
		{
			recipe = "none";
			clicksLeft = -1;
		}
		markDirty();

		/* IMPORTANT */
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		clicksLeft = -1;
		markDirty();

		/* IMPORTANT */
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
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

	/**
	 * @return true if the left click to break block needs to be canceled
	 */
	public boolean LeftClick(ItemStack tool)
	{
		if (clicksLeft < 0)
			return false;
		if (recipe.equals("none"))
			return false;
		Item item = tool.getItem();
		if (item instanceof ItemAxe)
		{
			clicksLeft--;
			if (clicksLeft <= 0)
			{
				ChoppingBlockRecipe getRecipe = ChoppingBlockRecipe.GetRecipe(recipe);
				recipe = "none";
				int count = slot.getStackInSlot(0).getCount();
				if (item == FAItems.choppingBlade || item == Items.WOODEN_AXE || item == Items.STONE_AXE)
				{
					ItemStack output = getRecipe.GetOutput();
					ItemStack copy = output.copy();
					copy.setCount(count * output.getCount() / 2);
					slot.setStackInSlot(0, copy);
				} else
				{
					ItemStack output = getRecipe.GetOutput();
					ItemStack copy = output.copy();
					copy.setCount(count * output.getCount());
					slot.setStackInSlot(0, copy);
				}
				craftsDone += count;
				if (craftsDone > craftsBeforeBreak)
				{
					DropItems();
					world.destroyBlock(pos, false);
				}
				markDirty();

				/* IMPORTANT */
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);
			}
			ItemHelper.DamageItem(tool);
		}

		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		clicksLeft = tag.getInteger("clicksLeft");
		slot.deserializeNBT(tag.getCompoundTag("slot"));
		craftsBeforeBreak = tag.getInteger("craftsBeforeBreak");
		craftsDone = tag.getInteger("craftsDone");
		recipe = tag.getString("recipe");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("clicksLeft", clicksLeft);
		tag.setInteger("craftsBeforeBreak", craftsBeforeBreak);
		tag.setInteger("craftsDone", craftsDone);
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

	public void DropItems()
	{
		if (!world.isRemote && !slot.getStackInSlot(0).isEmpty())
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack GetRenderStack()
	{
		return slot.getStackInSlot(0);
	}
}
