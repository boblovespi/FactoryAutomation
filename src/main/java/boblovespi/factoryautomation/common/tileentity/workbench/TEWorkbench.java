package boblovespi.factoryautomation.common.tileentity.workbench;

import boblovespi.factoryautomation.api.recipe.IWorkbenchRecipe;
import boblovespi.factoryautomation.api.recipe.WorkbenchPart;
import boblovespi.factoryautomation.api.recipe.WorkbenchRecipeHandler;
import boblovespi.factoryautomation.api.recipe.WorkbenchTool;
import boblovespi.factoryautomation.common.util.SetBlockStateFlags;
import com.mojang.authlib.GameProfile;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Willi on 4/8/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public abstract class TEWorkbench extends BlockEntity implements MenuProvider
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

	public TEWorkbench(BlockEntityType<? extends TEWorkbench> type, int size, int tier, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
		this.size = size;
		this.tier = tier;
		firstPartIndex = 2 + size;
		firstCraftingIndex = 2 + size * 2;
		inventory = new ItemStackHandler(size * size + size * 2 + 2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();

				if (Objects.requireNonNull(level).isClientSide)
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

							if (toolInfo.getKey().IsSameTool(tool) && toolInfo.getKey().tier <= Objects.requireNonNull(tool).tier)
							{
								getStackInSlot(i + firstToolIndex).hurtAndBreak(toolInfo.getValue(),
										FakePlayerFactory.get((ServerLevel) level, new GameProfile(null, "fakePlayer")),
										n -> {

										});
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

							if (partInfo.getKey().IsSamePart(part) && partInfo.getKey().tier <= Objects.requireNonNull(part).tier)
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

				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(),
						SetBlockStateFlags.SEND_TO_CLIENT | SetBlockStateFlags.FORCE_BLOCK_UPDATE
								| SetBlockStateFlags.PREVENT_RERENDER);
			}
		};

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
	public void load(CompoundTag compound)
	{
		super.load(compound);
		inventory.deserializeNBT(compound.getCompound("items"));
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		compound.put("items", inventory.serializeNBT());
	}

	public void CheckForRecipe()
	{
		Optional<IWorkbenchRecipe> recipeOptional = Objects.requireNonNull(level).getRecipeManager()
														 .getAllRecipesFor(WorkbenchRecipeHandler.WORKBENCH_RECIPE_TYPE)
														 .stream()
														 .filter(n -> n.CanFitTier(size, size, tier)).filter(n -> n
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
	 * Override instead of adding {@code if (firstTick)} stuff in update.
	 */
	@Override
	public void onLoad()
	{
		// CheckForRecipe();
	}

	@Override
	public Component getDisplayName()
	{
		return null;
	}
}
