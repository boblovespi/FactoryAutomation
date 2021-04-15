package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.MOLD;

/**
 * Created by Willi on 12/29/2018.
 */
public class TEStoneCastingVessel extends TileEntity
		implements ITickableTileEntity, ICastingVessel, INamedContainerProvider
{
	private boolean hasSand;
	private TEStoneCrucible.MetalForms form;
	private ItemStackHandler slot;
	private float temp = 20f;
	private int counter = 0;
	private boolean firstTick = true;

	public TEStoneCastingVessel()
	{
		super(TileEntityHandler.teStoneCastingVessel);
		slot = new ItemStackHandler(1);
		form = TEStoneCrucible.MetalForms.NONE;
	}

	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	public void FirstLoad()
	{
		form = getBlockState().get(MOLD).metalForm;
		firstTick = false;
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
		world.sendBlockUpdated(pos, getBlockState(), getBlockState(), 7);
	}

	@Override
	public float GetLoss()
	{
		return 1.5f;
	}

	public void DropItems()
	{
		if (!world.isClientSide && !slot.getStackInSlot(0).isEmpty())
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		markDirty();

		/* IMPORTANT */
		world.sendBlockUpdated(pos, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
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
						new StringTextComponent("Too hot: " + String.format("%1$.1f\u00b0C", temp)), true);
			}
		} else if (item.getItem() == Item.getItemFromBlock(FABlocks.greenSand.ToBlock())
				&& getBlockState().get(MOLD) == CastingVesselStates.EMPTY)
		{
			item.shrink(1);
			SetForm(CastingVesselStates.SAND);

			markDirty();
			/* IMPORTANT */
			world.sendBlockUpdated(pos, getBlockState(), getBlockState(), 7);
		}
	}

	public void SetForm(CastingVesselStates state)
	{
		if (state == CastingVesselStates.EMPTY)
			hasSand = false;
		else
			hasSand = true;
		world.setBlockState(pos, world.getBlockState(pos).with(MOLD, state));
		form = state.metalForm;
	}

	@Override
	public boolean HasSpace()
	{
		return slot.getStackInSlot(0).isEmpty();
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		slot.deserializeNBT(tag.getCompound("slot"));
		hasSand = tag.getBoolean("hasSand");
		temp = tag.getFloat("temp");
		form = TEStoneCrucible.MetalForms.values()[tag.getInt("form")];
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("slot", slot.serializeNBT());
		tag.putBoolean("hasSand", hasSand);
		tag.putFloat("temp", temp);
		tag.putInt("form", form.ordinal());
		return super.write(tag);
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
	public void tick()
	{
		if (world.isClientSide)
			return;
		if (firstTick)
			FirstLoad();
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
			world.sendBlockUpdated(pos, getBlockState(), getBlockState(), 7);
			counter = 10;
		}
	}

	public float GetTemp()
	{
		return temp;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerStoneCastingVessel(id, playerInv, pos);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(pos, 0, nbt);
	}
}