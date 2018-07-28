package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.recipe.SteelmakingRecipe;
import boblovespi.factoryautomation.common.block.machine.SteelmakingFurnaceController;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.MultiFluidTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willi on 12/24/2017.
 */
public class TESteelmakingFurnace extends TileEntity implements ITickable, ICapabilityProvider, IMultiblockControllerTE
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

	public TESteelmakingFurnace()
	{
		itemHandler = new ItemStackHandler(11);
		fluidHandler = new MultiFluidTank(2, Fluid.BUCKET_VOLUME * 10);
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
				world.getBlockState(pos).getValue(SteelmakingFurnaceController.AXIS) == EnumFacing.Axis.X ?
						EnumFacing.WEST : EnumFacing.NORTH);
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID,
				world.getBlockState(pos).getValue(SteelmakingFurnaceController.AXIS) == EnumFacing.Axis.X ?
						EnumFacing.WEST : EnumFacing.NORTH);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
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
					currentMaxBurnTime = TileEntityFurnace.getItemBurnTime(fuel);
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
		IBlockState state = world.getBlockState(pos);
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

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) itemHandler;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) fluidHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		currentSmeltTime = tag.getFloat("currentSmeltTime");
		currentMaxSmeltTime = tag.getFloat("currentMaxSmeltTime");

		currentTemp = tag.getFloat("currentTemp");

		currentBurnTime = tag.getFloat("currentBurnTime");
		currentMaxBurnTime = tag.getFloat("currentMaxBurnTime");

		isBurningFuel = tag.getBoolean("isBurningFuel");
		isSmeltingItem = tag.getBoolean("isSmeltingItem");

		itemHandler.deserializeNBT(tag.getCompoundTag("itemHandler"));

		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setFloat("currentSmeltTime", currentSmeltTime);
		tag.setFloat("currentMaxSmeltTime", currentMaxSmeltTime);

		tag.setFloat("currentTemp", currentTemp);

		tag.setFloat("currentBurnTime", currentBurnTime);
		tag.setFloat("currentMaxBurnTime", currentMaxBurnTime);

		tag.setBoolean("isBurningFuel", isBurningFuel);
		tag.setBoolean("isSmeltingItem", isSmeltingItem);

		tag.setTag("itemHandler", itemHandler.serializeNBT());

		return super.writeToNBT(tag);
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

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getTileData()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int meta = getBlockMetadata();

		return new SPacketUpdateTileEntity(pos, meta, nbt);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
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
}
