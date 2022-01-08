package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.CampfireRecipe;
import boblovespi.factoryautomation.common.block.processing.Campfire;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 12/27/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TECampfire extends BlockEntity implements ITickable
{
	private final ItemStackHandler slot;
	private String recipe = "none";
	private int timeLeft = -1;
	private boolean isLit = false;

	public TECampfire(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teCampfire, pos, state);
		slot = new ItemStackHandler(1);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide)
			return;
		if ("none".equals(recipe))
			return;
		if (!isLit)
			return;
		timeLeft--;
		if (timeLeft < 0)
		{
			CampfireRecipe getRecipe = level.getRecipeManager().getAllRecipesFor(CampfireRecipe.TYPE).stream().filter(r -> r.GetName().equals(recipe)).findFirst().orElse(null);
			ItemStack output = getRecipe.GetOutput();
			slot.setStackInSlot(0, output.copy());
			recipe = "none";
			timeLeft = -1;

			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
		setChanged();
	}

	public void DropItems()
	{
		if (!Objects.requireNonNull(level).isClientSide && !slot.getStackInSlot(0).isEmpty())
			level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack PlaceItem(ItemStack items)
	{
		if (!slot.getStackInSlot(0).isEmpty())
			return items;
		ItemStack stack = slot.insertItem(0, items, false);
		CampfireRecipe getRecipe = level.getRecipeManager().getAllRecipesFor(CampfireRecipe.TYPE).stream().filter(r -> r.GetInput().test(slot.getStackInSlot(0))).findFirst().orElse(null);
		if (getRecipe != null)
		{
			recipe = getRecipe.GetName();
			timeLeft = getRecipe.GetTime();
			isLit = Objects.requireNonNull(level).getBlockState(worldPosition).getValue(Campfire.LIT);
		} else
		{
			recipe = "none";
			timeLeft = -1;
		}
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		timeLeft = -1;
		recipe = "none";
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 7);
		return stack;
	}

	public void TakeOrPlace(ItemStack item, Player player)
	{
		if (!slot.getStackInSlot(0).isEmpty())
		{
			ItemStack taken = TakeItem();
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
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
	public void load(CompoundTag tag)
	{
		super.load(tag);
		timeLeft = tag.getInt("timeLeft");
		slot.deserializeNBT(tag.getCompound("slot"));
		recipe = tag.getString("recipe");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("timeLeft", timeLeft);
		tag.putString("recipe", recipe);
		tag.put("slot", slot.serializeNBT());
	}

	public void SetLit(boolean isLit)
	{
		this.isLit = isLit;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}
}
