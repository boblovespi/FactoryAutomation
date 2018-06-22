package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.recipe.BasicCircuitRecipe;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.IGuiElement;
import boblovespi.factoryautomation.common.util.SetBlockStateFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Willi on 5/28/2018.
 */
public class TEBasicCircuitCreator extends TileEntity
{
	private Layout layout;
	private ItemStackHandler inventory;

	public TEBasicCircuitCreator()
	{
		inventory = new ItemStackHandler(5)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}
		};
		layout = new Layout();
	}

	public void Craft()
	{
		if (world.isRemote)
			return;

		BasicCircuitRecipe recipe = BasicCircuitRecipe.FindRecipe(layout.grid);

		if (recipe != null)

		{
			if (!inventory.getStackInSlot(0).isEmpty())
				return;
			if (inventory.extractItem(4, 1, false).isEmpty())
				return;

			inventory.insertItem(0, recipe.GetResult(), false);
			layout = new Layout();

			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos),
					SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
							| SetBlockStateFlags.PREVENT_RERENDER);
		}
	}

	public void AddComponent(Layout.Element e, int x, int y)
	{
		if (world.isRemote)
			return;

		if (layout.Get(x, y).equals(e))
			return;
		RemoveComponentInternal(x, y);
		switch (e)
		{
		case SOLDER:
			if (inventory.extractItem(1, 1, false).isEmpty())
				return;
			break;
		case WIRE:
			if (inventory.extractItem(2, 1, false).isEmpty())
				return;
			break;
		case CHIP:
			if (inventory.extractItem(3, 1, true).getItem() != FAItems.basicChip)
				return;
			inventory.extractItem(3, 1, false);
			RemoveComponentInternal(x + 1, y);
			RemoveComponentInternal(x, y + 1);
			RemoveComponentInternal(x + 1, y + 1);
			break;
		}
		layout.Set(e, x, y);
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos),
				SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
						| SetBlockStateFlags.PREVENT_RERENDER);
	}

	public void RemoveComponent(int x, int y)
	{
		RemoveComponentInternal(x, y);
		markDirty();
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos),
				SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
						| SetBlockStateFlags.PREVENT_RERENDER);
	}

	private void RemoveComponentInternal(int x, int y)
	{
		if (world.isRemote)
			return;
		Layout.Element e = layout.Get(x, y);
		switch (e)
		{
		case EMPTY:
			return;
		case SOLDER:
			if (!inventory.insertItem(1, new ItemStack(FAItems.nugget.GetItem(Metals.TIN)), false).isEmpty())
				return;
			break;
		case WIRE:
			if (!inventory.insertItem(2, new ItemStack(FAItems.copperWire.ToItem()), false).isEmpty())
				return;
			break;
		}

		layout.Set(Layout.Element.EMPTY, x, y);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("items"));
		layout.Deserialize(compound.getTagList("layout", 11));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setTag("items", inventory.serializeNBT());
		compound.setTag("layout", layout.Serialize());
		return super.writeToNBT(compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		//noinspection MethodCallSideOnly
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

	public Layout.Element[][] GetComponents()
	{
		return layout.grid.clone();
	}

	public static class Layout
	{
		private Element[][] grid = new Element[8][8];

		public Layout(Element[][] grid)
		{
			this.grid = grid;
		}

		public Layout()
		{
			for (Element[] temp : grid)
				Arrays.fill(temp, Element.EMPTY);
		}

		public Layout Set(Element e, int x, int y)
		{
			grid[y][x] = e;
			return this;
		}

		public Element Get(int x, int y)
		{
			return grid[y][x];
		}

		public NBTTagList Serialize()
		{
			NBTTagList list = new NBTTagList();

			for (int i = 0; i < grid.length; i++)
			{
				int[] arr = new int[grid[i].length];

				for (int j = 0; j < grid[i].length; j++)
				{
					arr[j] = grid[i][j].ordinal();
				}

				list.appendTag(new NBTTagIntArray(arr));
			}

			return list;
		}

		public void Deserialize(NBTTagList list)
		{
			for (int i = 0; i < grid.length; i++)
			{
				int[] arr = list.getIntArrayAt(i);

				for (int j = 0; j < grid[i].length; j++)
				{
					grid[i][j] = Element.values()[arr[j]];
				}
			}
		}

		public enum Element implements IGuiElement
		{
			SOLDER(0, 0),
			WIRE(2, 2),
			CHIP(2, 2, 3, 0),
			RED_CHIP(2, 2, 5, 0),
			CHANNEL_TUNER(3, 2, 7, 0),
			ANTENNA_PORT(2, 1, 3, 2),
			EMPTY(0, 7);
			public final int sizeX;
			public final int sizeY;
			public final int u;
			public final int v;

			Element(int sizeX, int sizeY, int u, int v)
			{
				this.sizeX = sizeX;
				this.sizeY = sizeY;
				this.v = v;
				this.u = u;
			}

			Element(int u, int v)
			{
				sizeX = 1;
				sizeY = 1;
				this.u = u;
				this.v = v;
			}

			@Override
			public int GetX()
			{
				return u;
			}

			@Override
			public int GetY()
			{
				return v;
			}

			@Override
			public int GetU()
			{
				return sizeX;
			}

			@Override
			public int GetV()
			{
				return sizeY;
			}
		}
	}
}
