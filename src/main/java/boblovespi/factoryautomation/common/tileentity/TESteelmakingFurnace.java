package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.common.block.machine.SteelmakingFurnaceController;
import boblovespi.factoryautomation.common.container.ContainerSteelmakingFurnace;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.MultiFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.common.block.machine.SteelmakingFurnaceController.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/24/2017.
 */
public class TESteelmakingFurnace extends TileEntity
		implements ITickableTileEntity, IMultiblockControllerTE, INamedContainerProvider
{
	public static final String MULTIBLOCK_ID = "steelmaking_furnace";

	public static final int[] INPUT_SLOTS = { 0, 1, 2, 3 };
	public static final int[] OUTPUT_SLOTS = { 4, 5, 6, 7 };
	public static final int FUEL_SLOT = 8;
	public static final int AIR_INPUT_SLOT = 9;
	public static final int FUEL_INPUT_SLOT = 10; // not to be confused with the furnace fuel input slot, this one is used for recipes

	private ItemStackHandler itemHandler;
	private MultiFluidTank fluidHandler;
	private boolean isValid;

	private float currentSmeltTime = 1;
	private float currentMaxSmeltTime = 1;
	private float currentTemp = 0;
	private float maxTemp = 1500;
	private float currentBurnTime = 0;
	private float currentMaxBurnTime = 1;

	private float smeltScalar = 1;
	private float burnScalar = 1;
	private float tempSpeedScalar = 1;

	private boolean isBurningFuel = false;
	private boolean isSmeltingItem = false;

	private SteelmakingRecipe currentRecipe = null;
	private IIntArray containerInfo = new IIntArray()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return (int) (GetBurnPercent() * 100);
			case 1:
				return (int) (GetTempPercent() * 100);
			case 2:
				return (int) (GetSmeltPercent() * 100);
			case 3:
				return (int) (GetTemp() * 10);
			}
			return 0;
		}

		@Override
		public void set(int index, int value)
		{

		}

		@Override
		public int size()
		{
			return 4;
		}
	};

	public TESteelmakingFurnace()
	{
		super(TileEntityHandler.teSteelmakingFurnace);
		itemHandler = new ItemStackHandler(11);
		fluidHandler = new MultiFluidTank(2, 1000/*magic number moment*/ * 10);
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		this.isValid = isValid;
	}

	@Override
	public boolean IsStructureValid()
	{
		return isValid;
	}

	@Override
	public void CreateStructure()
	{
		MultiblockHelper.CreateStructure(world, pos, MULTIBLOCK_ID,
				world.getBlockState(pos).get(SteelmakingFurnaceController.AXIS) == Direction.Axis.X ? Direction.WEST :
						Direction.NORTH);
		world.setBlockState(pos, world.getBlockState(pos).with(MULTIBLOCK_COMPLETE, true));
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID,
				world.getBlockState(pos).get(SteelmakingFurnaceController.AXIS) == Direction.Axis.X ? Direction.WEST :
						Direction.NORTH);
		world.setBlockState(pos, world.getBlockState(pos).with(MULTIBLOCK_COMPLETE, false));
	}

	@Override
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		return LazyOptional.empty();
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		// Log.LogInfo("heat", currentTemp);
		if (world.isRemote)
			return;
		if (!isSmeltingItem)
		{
			if (!isBurningFuel)
			{
				currentTemp -= tempSpeedScalar;
				if (currentTemp < 0)
					currentTemp = 0;

				if (!itemHandler.getStackInSlot(FUEL_SLOT).isEmpty())
				{
					ItemStack fuel = itemHandler.getStackInSlot(FUEL_SLOT);

					itemHandler.extractItem(FUEL_SLOT, 1, false);
					isBurningFuel = true;
					currentMaxBurnTime = fuel.getBurnTime();
					currentBurnTime = currentMaxBurnTime;
				}

			}
			if (isBurningFuel)
			{
				currentBurnTime -= burnScalar;
				currentTemp += tempSpeedScalar;
				if (currentBurnTime < 0)
				{
					currentBurnTime = 0;
					isBurningFuel = false;
				}

				if (currentTemp > maxTemp)
				{
					currentTemp = maxTemp;
				}

			}

			SteelmakingRecipe r = GetRecipe();

			if (r != null && CanInsertOutputs(r))
			{
				//				Log.LogInfo("Recipe found!");
				//				Log.LogInfo("Time required", r.timeRequired);
				//				Log.LogInfo("Temp required", r.tempRequired);

				if (currentTemp >= r.tempRequired) // we can begin smelting the recipe r
				{
					isSmeltingItem = true;
					currentMaxSmeltTime = r.timeRequired;
					currentSmeltTime = currentMaxSmeltTime;

					currentRecipe = r;

				}
			}
			//			} else
			//				Log.LogInfo("No recipe found");
		}
		if (isSmeltingItem)
		{
			currentSmeltTime -= smeltScalar;

			currentRecipe = GetRecipe();
			if (currentRecipe == null)
			{
				isSmeltingItem = false;
				currentSmeltTime = currentMaxSmeltTime;
			}

			if (currentSmeltTime < 0) // we are done smelting
			{
				if (CanInsertOutputs(currentRecipe))
				{
					for (int i = 0; i < currentRecipe.GetItemInputs().size(); i++)
					{
						itemHandler.extractItem(INPUT_SLOTS[i], 1, false);
					}

					for (int i = 0; i < currentRecipe.GetPrimaryItemOutputs().size(); i++)
					{
						// Log.LogInfo("i", i);
						// Log.LogInfo("Outputting!",
						itemHandler.insertItem(OUTPUT_SLOTS[i], currentRecipe.GetPrimaryItemOutputs().get(i).copy(),
								false);
					}

					isSmeltingItem = false;
					currentSmeltTime = currentMaxSmeltTime;
				}
			}
		}

		markDirty();

		/* IMPORTANT */
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	private boolean CanInsertOutputs(SteelmakingRecipe r)
	{
		int num = r.GetPrimaryItemOutputs().size();

		if (num > 4)
			return false;
		for (int i = 0; i < num; i++)
		{
			if (!itemHandler.insertItem(OUTPUT_SLOTS[i], r.GetPrimaryItemOutputs().get(i).copy(), true).isEmpty())
				return false;
		}

		return true;
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) itemHandler);
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) fluidHandler);
		return super.getCapability(capability, facing);
	}

	@Override
	public void read(CompoundNBT tag)
	{
		currentSmeltTime = tag.getFloat("currentSmeltTime");
		currentMaxSmeltTime = tag.getFloat("currentMaxSmeltTime");

		currentTemp = tag.getFloat("currentTemp");

		currentBurnTime = tag.getFloat("currentBurnTime");
		currentMaxBurnTime = tag.getFloat("currentMaxBurnTime");

		isBurningFuel = tag.getBoolean("isBurningFuel");
		isSmeltingItem = tag.getBoolean("isSmeltingItem");

		itemHandler.deserializeNBT(tag.getCompound("itemHandler"));

		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.putFloat("currentSmeltTime", currentSmeltTime);
		tag.putFloat("currentMaxSmeltTime", currentMaxSmeltTime);

		tag.putFloat("currentTemp", currentTemp);

		tag.putFloat("currentBurnTime", currentBurnTime);
		tag.putFloat("currentMaxBurnTime", currentMaxBurnTime);

		tag.putBoolean("isBurningFuel", isBurningFuel);
		tag.putBoolean("isSmeltingItem", isSmeltingItem);

		tag.put("itemHandler", itemHandler.serializeNBT());

		return super.write(tag);
	}

	private SteelmakingRecipe GetRecipe()
	{
		List<ItemStack> items = new ArrayList<>(4);
		for (int i : INPUT_SLOTS)
		{
			if (!itemHandler.getStackInSlot(i).isEmpty())
				items.add(itemHandler.getStackInSlot(i));
		}

		return SteelmakingRecipe.FindRecipe(items, fluidHandler.GetFluids());
	}

	@SuppressWarnings("MethodCallSideOnly")
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

	public float GetTempPercent()
	{
		return currentTemp / maxTemp;
	}

	public float GetSmeltPercent()
	{
		return (currentMaxSmeltTime - currentSmeltTime) / currentMaxSmeltTime;
	}

	public float GetBurnPercent()
	{
		return currentBurnTime / currentMaxBurnTime;
	}

	public float GetTemp()
	{
		return currentTemp;
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
		return new ContainerSteelmakingFurnace(id, playerInv, itemHandler, containerInfo, pos);
	}
}
