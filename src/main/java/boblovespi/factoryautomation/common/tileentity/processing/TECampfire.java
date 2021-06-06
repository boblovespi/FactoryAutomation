package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.CampfireRecipe;
import boblovespi.factoryautomation.common.block.processing.Campfire;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 12/27/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TECampfire extends TileEntity implements ITickableTileEntity
{
	private final ItemStackHandler slot;
	private String recipe = "none";
	private int timeLeft = -1;
	private boolean isLit = false;

	public TECampfire()
	{
		super(TileEntityHandler.teCampfire);
		slot = new ItemStackHandler(1);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(world).isClientSide)
			return;
		if ("none".equals(recipe))
			return;
		if (!isLit)
			return;
		timeLeft--;
		if (timeLeft < 0)
		{
			CampfireRecipe getRecipe = CampfireRecipe.getRecipe(recipe);
			ItemStack output = getRecipe.getOutput();
			slot.setStackInSlot(0, output.copy());
			recipe = "none";
			timeLeft = -1;

			world.sendBlockUpdated(levelPosition, getBlockState(), getBlockState(), 3);
		}
		setChanged();
	}

	public void DropItems()
	{
		if (!Objects.requireNonNull(world).isClientSide && !slot.getStackInSlot(0).isEmpty())
			world.addFreshEntity(new ItemEntity(world, levelPosition.getX(), levelPosition.getY(), levelPosition.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack PlaceItem(ItemStack items)
	{
		if (!slot.getStackInSlot(0).isEmpty())
			return items;
		ItemStack stack = slot.insertItem(0, items, false);
		CampfireRecipe getRecipe = CampfireRecipe.findRecipe(slot.getStackInSlot(0));
		if (getRecipe != null)
		{
			recipe = getRecipe.getName();
			timeLeft = getRecipe.getTime();
			isLit = Objects.requireNonNull(world).getBlockState(levelPosition).getValue(Campfire.LIT);
		} else
		{
			recipe = "none";
			timeLeft = -1;
		}
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(world).sendBlockUpdated(levelPosition, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		timeLeft = -1;
		recipe = "none";
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(world).sendBlockUpdated(levelPosition, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
	{
		if (!slot.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = TakeItem();
			ItemHelper.putItemsInInventoryOrDrop(player, taken, world);
		} else
		{
			ItemStack stack = PlaceItem(item.copy().split(1));
			int itemsTaken = 1 - stack.getCount();
			item.shrink(itemsTaken);
		}
	}

	public ItemStack GetRenderStack()
	{
		return slot.getStackInSlot(0);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		timeLeft = tag.getInt("timeLeft");
		slot.deserializeNBT(tag.getCompound("slot"));
		recipe = tag.getString("recipe");
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.putInt("timeLeft", timeLeft);
		tag.putString("recipe", recipe);
		tag.put("slot", slot.serializeNBT());
		return super.save(tag);
	}

	public void SetLit(boolean isLit)
	{
		this.isLit = isLit;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		save(pkt.getTag());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(levelPosition, 0, nbt);
	}
}
