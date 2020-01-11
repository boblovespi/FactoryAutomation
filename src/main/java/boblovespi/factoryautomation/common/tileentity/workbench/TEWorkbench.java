package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchPart;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.api.recipe.WorkbenchTool;
import boblovespi.factoryautomation.common.util.SetBlockStateFlags;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Willi on 4/8/2018.
 */
public abstract class TEWorkbench extends TileEntity
{
	protected final int size;
	protected final int tier;
	protected final int firstToolIndex = 2;
	protected final int firstPartIndex;
	protected final int firstCraftingIndex;
	protected ItemStackHandler inventory;
	protected ItemStack output = ItemStack.EMPTY;
	protected IWorkbenchRecipe recipe = null;
	private boolean isUpdatingChanges = false;

	public TEWorkbench(int size, int tier)
	{
		this.size = size;
		this.tier = tier;
		firstPartIndex = 2 + size;
		firstCraftingIndex = 2 + size * 2;
		inventory = new ItemStackHandler(size * size + size * 2 + 2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();

				if (world.isRemote)
					return;

				if (!isUpdatingChanges && slot == 0 && recipe != null && getStackInSlot(0).isEmpty() && !output
						.isEmpty())
				{
					isUpdatingChanges = true;
					for (int i = 0; i < size * size; i++)
					{
						extractItem(i + firstCraftingIndex, 1, false);
					}

					HashMap<WorkbenchTool.Instance, Integer> tools = recipe.GetToolDurabilityUsage();
					for (Map.Entry<WorkbenchTool.Instance, Integer> toolInfo : tools.entrySet())
					{
						for (int i = 0; i < (size); i++)
						{
							WorkbenchTool.Instance tool = WorkbenchTool.Instance
									.FromToolStack(getStackInSlot(i + firstToolIndex));

							if (toolInfo.getKey().IsSameTool(tool) && toolInfo.getKey().tier <= tool.tier)
							{
								getStackInSlot(i + firstToolIndex).damageItem(
										toolInfo.getValue(), FakePlayerFactory
												.get((WorldServer) world, new GameProfile(null, "fakePlayer")));
								break;
							}
						}
					}

					/*for (int i = 0; i < size; i++)
					{
						ItemStack tool = getStackInSlot(i + firstToolIndex);
						WorkbenchTool.Instance toolInstance = WorkbenchTool.Instance.FromToolStack(tool);

						Optional<WorkbenchTool.Instance> first = tools.keySet().stream().filter(recipeTool -> recipeTool
								.IsSameTool(toolInstance)).filter(recipeTool -> recipeTool.tier <= toolInstance.tier)
																	  .findFirst();
						if (first.isPresent())
						{
							Integer durability = tools.getOrDefault(first.get(), null);
							if (durability != null)
								tool.damageItem(durability, null);
						}
					}*/

					HashMap<WorkbenchPart.Instance, Integer> parts = recipe.GetPartUsage();
					for (Map.Entry<WorkbenchPart.Instance, Integer> partInfo : parts.entrySet())
					{
						for (int i = 0; i < (size); i++)
						{
							WorkbenchPart.Instance part = WorkbenchPart.Instance
									.FromPartStack(getStackInSlot(i + firstPartIndex));

							if (partInfo.getKey().IsSamePart(part) && partInfo.getKey().tier <= part.tier)
							{
								getStackInSlot(i + firstPartIndex).shrink(partInfo.getValue());
								break;
							}
						}
					}
					/*for (int i = 0; i < size; i++)
					{
						ItemStack part = getStackInSlot(i + firstPartIndex);
						WorkbenchPart.Instance partInstance = WorkbenchPart.Instance.FromPartStack(part);

						Optional<WorkbenchPart.Instance> first = parts.keySet().stream().filter(recipePart -> recipePart
								.IsSamePart(partInstance)).filter(recipePart -> recipePart.tier <= partInstance.tier)
																	  .findFirst();
						if (first.isPresent())
						{
							Integer durability = parts.getOrDefault(first.get(), null);
							if (durability != null)
								part.damageItem(durability, null);
						}
					}*/
					isUpdatingChanges = false;
				}

				// System.out.println("contents changed in slot " + slot);
				if (!isUpdatingChanges)
					CheckForRecipe();

				BlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state,
						SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
								| SetBlockStateFlags.PREVENT_RERENDER);
			}
		};

	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(CompoundNBT compound)
	{
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("items"));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound)
	{
		compound.setTag("items", inventory.serializeNBT());
		return super.writeToNBT(compound);
	}

	public void CheckForRecipe()
	{
		Optional<IWorkbenchRecipe> recipeOptional = WorkbenchRecipeHandler.recipes.values().stream().filter(n -> n
				.CanFitTier(size, size, tier)).filter(n -> n
				.Matches(inventory, size <= 3, firstToolIndex, firstPartIndex, firstCraftingIndex)).findFirst();
		recipe = recipeOptional.orElse(null);
		if (recipe != null)
			output = recipe.GetResult(inventory);
		else
			output = null;
		isUpdatingChanges = true;
		if (output != null)
			inventory.setStackInSlot(0, output.copy());
		else
			inventory.setStackInSlot(0, ItemStack.EMPTY);
		isUpdatingChanges = false;
	}

	/**
	 * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad()
	{
		CheckForRecipe();
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getTileData()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}
}
