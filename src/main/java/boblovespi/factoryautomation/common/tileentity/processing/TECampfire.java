package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.CampfireRecipe;
import boblovespi.factoryautomation.common.block.processing.Campfire;
import boblovespi.factoryautomation.common.handler.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Willi on 12/27/2018.
 */
public class TECampfire extends TileEntity implements ITickableTileEntity
{
	private ItemStackHandler slot;
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

			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
		}
		markDirty();
	}

	public void DropItems()
	{
		if (!world.isRemote && !slot.getStackInSlot(0).isEmpty())
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), slot.getStackInSlot(0)));
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
			isLit = world.getBlockState(pos).get(Campfire.LIT);
		} else
		{
			recipe = "none";
			timeLeft = -1;
		}
		markDirty();

		/* IMPORTANT */
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		timeLeft = -1;
		recipe = "none";
		markDirty();

		/* IMPORTANT */
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, PlayerEntity player)
	{
		if (!slot.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = TakeItem();
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, world);
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
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		timeLeft = tag.getInt("timeLeft");
		slot.deserializeNBT(tag.getCompound("slot"));
		recipe = tag.getString("recipe");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.putInt("timeLeft", timeLeft);
		tag.putString("recipe", recipe);
		tag.put("slot", slot.serializeNBT());
		return super.write(tag);
	}

	public void SetLit(boolean isLit)
	{
		this.isLit = isLit;
	}
}
