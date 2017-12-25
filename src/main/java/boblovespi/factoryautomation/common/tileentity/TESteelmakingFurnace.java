package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.common.multiblock.IMultiblockStructureControllerTileEntity;
import boblovespi.factoryautomation.common.util.MultiFluidTank;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Willi on 12/24/2017.
 */
public class TESteelmakingFurnace extends TileEntity
		implements ITickable, ICapabilityProvider,
		IMultiblockStructureControllerTileEntity
{
	public static final int[] INPUT_SLOTS = { 0, 1, 2, 3 };
	public static final int[] OUTPUT_SLOTS = { 4, 5, 6, 7 };
	public static final int FUEL_SLOT = 8;
	public static final int AIR_INPUT_SLOT = 9;
	public static final int FUEL_INPUT_SLOT = 10; // not to be confused with the furnace fuel input slot, this one is used for recipes

	private ItemStackHandler itemHandler;
	private MultiFluidTank fluidHandler;

	private boolean isValid;

	public TESteelmakingFurnace()
	{
		itemHandler = new ItemStackHandler(11);
		fluidHandler = new MultiFluidTank(2);
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

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{

	}

	@Override
	public boolean hasCapability(Capability<?> capability,
			@Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability,
			@Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) itemHandler;
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) fluidHandler;
		return super.getCapability(capability, facing);
	}
}
