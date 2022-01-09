package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 12/26/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEChoppingBlock extends BlockEntity
{
	private final int maxClicks = 5;
	private int clicksLeft = -1;
	private final ItemStackHandler slot;
	private int craftsBeforeBreak;
	private int craftsDone = 0;
	private String recipe = "none";

	public TEChoppingBlock(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teChoppingBlock, pos, state);
		slot = new ItemStackHandler(1);
	}

	public TEChoppingBlock(int maxUses, BlockPos pos, BlockState state)
	{
		this(pos, state);
		craftsBeforeBreak = maxUses;
	}

	public ItemStack PlaceItem(ItemStack items)
	{
		if (!slot.getStackInSlot(0).isEmpty())
			return items;
		ItemStack stack = slot.insertItem(0, items, false);
		ChoppingBlockRecipe getRecipe = level.getRecipeManager().getAllRecipesFor(ChoppingBlockRecipe.TYPE).stream().filter(r -> r.GetInput().test(slot.getStackInSlot(0))).findFirst().orElse(null);
		if (getRecipe != null)
		{
			recipe = getRecipe.GetName();
			clicksLeft = maxClicks;
		} else
		{
			recipe = "none";
			clicksLeft = -1;
		}
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		return stack;
	}

	public ItemStack TakeItem()
	{
		ItemStack stack = slot.extractItem(0, 64, false);
		clicksLeft = -1;
		setChanged();

		/* IMPORTANT */
		Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
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
		if (item instanceof AxeItem || FATags.FAItemTag("axes").contains(item))
		{
			clicksLeft--;
			if (clicksLeft <= 0)
			{
				ChoppingBlockRecipe getRecipe = level.getRecipeManager().getAllRecipesFor(ChoppingBlockRecipe.TYPE).stream().filter(r -> r.GetName().equals(recipe)).findFirst().orElse(null);
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
					Objects.requireNonNull(level).destroyBlock(worldPosition, false);
				}
				setChanged();

				/* IMPORTANT */
				Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
			ItemHelper.DamageItem(tool);
		}

		return true;
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		clicksLeft = tag.getInt("clicksLeft");
		slot.deserializeNBT(tag.getCompound("slot"));
		craftsBeforeBreak = tag.getInt("craftsBeforeBreak");
		craftsDone = tag.getInt("craftsDone");
		recipe = tag.getString("recipe");
	}

	@Override
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("clicksLeft", clicksLeft);
		tag.putInt("craftsBeforeBreak", craftsBeforeBreak);
		tag.putInt("craftsDone", craftsDone);
		tag.putString("recipe", recipe);
		tag.put("slot", slot.serializeNBT());
	}

	public void DropItems()
	{
		if (!Objects.requireNonNull(level).isClientSide && !slot.getStackInSlot(0).isEmpty())
			level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), slot.getStackInSlot(0)));
	}

	public ItemStack GetRenderStack()
	{
		return slot.getStackInSlot(0);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
	}
}
