package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.CastingVesselStates;
import boblovespi.factoryautomation.common.container.ContainerStoneCastingVessel;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.decoration.StoneCastingVessel.MOLD;

/**
 * Created by Willi on 12/29/2018.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEStoneCastingVessel extends TileEntity
		implements ITickableTileEntity, ICastingVessel, INamedContainerProvider
{
	private boolean hasSand;
	private TEStoneCrucible.MetalForms form;
	private final ItemStackHandler slot;
	private float temp = 20f;
	private int counter = 0;
	private boolean firstTick = true;
	private IIntArray formData = new IIntArray()
	{
		@Override
		public int get(int unused)
		{
			return form == TEStoneCrucible.MetalForms.NONE ? 0 : form.ordinal() + 1;
		}

		@Override
		public void set(int a, int b)
		{

		}

		@Override
		public int getCount()
		{
			return 1;
		}
	};

	public TEStoneCastingVessel()
	{
		super(TileEntityHandler.teStoneCastingVessel);
		slot = new ItemStackHandler(1);
		form = TEStoneCrucible.MetalForms.NONE;
	}

	/**
	 * Called when this is first added to the level (by {@link World#addBlockEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	public void FirstLoad()
	{
		form = getBlockState().getValue(MOLD).metalForm;
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
		setChanged();
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
	}

	@Override
	public float GetLoss()
	{
		return 1.5f;
	}

	// Todo: remove if not used, looks like it's unused. - Qboi123
	public void DropItems()
	{
		if (!Objects.requireNonNull(level).isClientSide && !slot.getStackInSlot(0).isEmpty())
			level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
	{
		if (!slot.getStackInSlot(0).isEmpty())
		{
			if (temp < 40f)
			{
				ItemStack taken = TakeItem();
				ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
				if (hasSand)
				{
					ItemHelper.PutItemsInInventoryOrDrop(player, new ItemStack(FABlocks.greenSand.ToBlock()), level);
					SetForm(CastingVesselStates.EMPTY);
				}
			} else
			{
				player.hurt(DamageSource.GENERIC, (temp - 40f) / (temp + 100f) * 20f);
				player.displayClientMessage(
						new StringTextComponent("Too hot: " + String.format("%1$.1f\u00b0C", temp)), true);
			}
		} else if (item.getItem() == Item.byBlock(FABlocks.greenSand.ToBlock())
				&& getBlockState().getValue(MOLD) == CastingVesselStates.EMPTY)
		{
			item.shrink(1);
			SetForm(CastingVesselStates.SAND);

			setChanged();
			/* IMPORTANT */
			Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
		}
	}

	public void SetForm(CastingVesselStates state)
	{
		// Todo: optimize if statement.
		if (state == CastingVesselStates.EMPTY)
			hasSand = false;
		else
			hasSand = true;
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(MOLD, state));
		form = state.metalForm;
	}

	@Override
	public boolean HasSpace()
	{
		return slot.getStackInSlot(0).isEmpty();
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		slot.deserializeNBT(tag.getCompound("slot"));
		hasSand = tag.getBoolean("hasSand");
		temp = tag.getFloat("temp");
		form = TEStoneCrucible.MetalForms.values()[tag.getInt("form")];
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("slot", slot.serializeNBT());
		tag.putBoolean("hasSand", hasSand);
		tag.putFloat("temp", temp);
		tag.putInt("form", form.ordinal());
		return super.save(tag);
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
		if (Objects.requireNonNull(level).isClientSide)
			return;
		if (firstTick)
			FirstLoad();
		if (temp > 20f)
		{
			if (level.isRaining())
				temp *= 0.9938f;
			else
				temp *= 0.9972f;
		}
		counter--;
		if (counter < 0)
		{
			setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
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
		return new StringTextComponent("");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerStoneCastingVessel(id, playerInv, worldPosition, formData);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(Objects.requireNonNull(level).getBlockState(worldPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(worldPosition, 0, nbt);
	}
}