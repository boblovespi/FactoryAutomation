package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.recipe.BasicCircuitRecipe;
import boblovespi.factoryautomation.common.container.ContainerBasicCircuitCreator;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.util.IGuiElement;
import boblovespi.factoryautomation.common.util.SetBlockStateFlags;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Willi on 5/28/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEBasicCircuitCreator extends TileEntity implements INamedContainerProvider
{
	private Layout layout;
	private final ItemStackHandler inventory;

	public TEBasicCircuitCreator()
	{
		super(TileEntityHandler.teBasicCircuitCreator);
		inventory = new ItemStackHandler(5)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
			}
		};
		layout = new Layout();
	}

	public void Craft()
	{
		if (Objects.requireNonNull(world).isClientSide)
			return;

		BasicCircuitRecipe recipe = BasicCircuitRecipe.findRecipe(layout.grid);

		if (recipe != null)

		{
			if (!inventory.getStackInSlot(0).isEmpty())
				return;
			if (inventory.extractItem(4, 1, false).isEmpty())
				return;

			inventory.insertItem(0, recipe.getResult(), false);
			layout = new Layout();

			setChanged();
			world.sendBlockUpdated(levelPosition, world.getBlockState(levelPosition), world.getBlockState(levelPosition),
					SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
							| SetBlockStateFlags.PREVENT_RERENDER);
		}
	}

	public void AddComponent(Layout.Element e, int x, int y)
	{
		if (Objects.requireNonNull(world).isClientSide)
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
		setChanged();
		world.sendBlockUpdated(levelPosition, world.getBlockState(levelPosition), world.getBlockState(levelPosition),
				SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
						| SetBlockStateFlags.PREVENT_RERENDER);
	}

	public void RemoveComponent(int x, int y)
	{
		RemoveComponentInternal(x, y);
		setChanged();
		Objects.requireNonNull(world).sendBlockUpdated(levelPosition, world.getBlockState(levelPosition), world.getBlockState(levelPosition),
				SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
						| SetBlockStateFlags.PREVENT_RERENDER);
	}

	private void RemoveComponentInternal(int x, int y)
	{
		if (Objects.requireNonNull(world).isClientSide)
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
			if (!inventory.insertItem(2, new ItemStack(FAItems.copperWire.toItem()), false).isEmpty())
				return;
			break;
		}

		layout.Set(Layout.Element.EMPTY, x, y);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) inventory);
		return super.getCapability(capability, facing);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		inventory.deserializeNBT(compound.getCompound("items"));
		layout.Deserialize(compound.getList("layout", 11));
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		compound.put("items", inventory.serializeNBT());
		compound.put("layout", layout.Serialize());
		return super.save(compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(world).getBlockState(levelPosition), pkt.getTag());
	}

	//	@Override
	//	public CompoundNBT getTileData()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		return nbt;
	//	}
	//
	//	@Override
	//	public CompoundNBT getUpdateTag()
	//	{
	//		CompoundNBT nbt = new CompoundNBT();
	//		writeToNBT(nbt);
	//		return nbt;
	//	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);

		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}

	public Layout.Element[][] GetComponents()
	{
		return layout.grid.clone();
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
		return new ContainerBasicCircuitCreator(id, playerInv, inventory, levelPosition);
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

		public ListNBT Serialize()
		{
			ListNBT list = new ListNBT();

			for (Element[] elements : grid) {
				int[] arr = new int[elements.length];

				for (int j = 0; j < elements.length; j++) {
					arr[j] = elements[j].ordinal();
				}

				list.add(new IntArrayNBT(arr));
			}

			return list;
		}

		public void Deserialize(ListNBT list)
		{
			for (int i = 0; i < grid.length; i++)
			{
				int[] arr = list.getIntArray(i);

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
			public int getX()
			{
				return u;
			}

			@Override
			public int getY()
			{
				return v;
			}

			@Override
			public int getU()
			{
				return sizeX;
			}

			@Override
			public int getV()
			{
				return sizeY;
			}
		}
	}
}
