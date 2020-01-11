package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser.HEAT_USER_CAPABILITY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

/**
 * Created by Willi on 10/28/2018.
 */
public class TESolidFueledFirebox extends TileEntity implements ITickable
{
	private HeatUser heatUser;
	private ItemStackHandler inventory;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;

	public TESolidFueledFirebox()
	{
		heatUser = new HeatUser(20, 1600, 300);
		inventory = new ItemStackHandler(1);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		float K_d = heatUser.GetTemperature() - 20f;
		float gamma = CapabilityHeatUser.AIR_CONDUCTIVITY;
		float transfer = K_d * gamma * 0.05f;
		heatUser.TransferEnergy(-transfer);
		ItemStack stack = inventory.getStackInSlot(0);
		if (isBurningFuel)
		{
			burnTime--;
			if (fuelInfo == FuelRegistry.NULL)
			{
				fuelInfo = FuelRegistry.GetInfo(stack.getItem(), stack.getItemDamage());
				if (fuelInfo == FuelRegistry.NULL)
				{
					burnTime = 0;
					maxBurnTime = 1;
					isBurningFuel = false;
				}
			}
			if (fuelInfo != FuelRegistry.NULL && heatUser.GetTemperature() <= fuelInfo.GetBurnTemp())
			{
				heatUser.TransferEnergy(fuelInfo.GetTotalEnergy() / (float) fuelInfo.GetBurnTime());
			}
			if (burnTime <= 0)
			{
				burnTime = 0;
				maxBurnTime = 1;
				isBurningFuel = false;
			}
		} else
		{
			if (!stack.isEmpty())
			{
				fuelInfo = FuelRegistry.GetInfo(stack.getItem(), stack.getItemDamage());
				if (fuelInfo != FuelRegistry.NULL)
				{
					burnTime = fuelInfo.GetBurnTime();
					maxBurnTime = fuelInfo.GetBurnTime();
					isBurningFuel = true;
					inventory.extractItem(0, 1, false);
				}
			}
		}

		markDirty();

		/* IMPORTANT */
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
	{
		if (capability == HEAT_USER_CAPABILITY && facing == Direction.UP)
			return true;
		if (capability == ITEM_HANDLER_CAPABILITY)
			return true;
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == HEAT_USER_CAPABILITY && facing == Direction.UP)
			return (T) heatUser;
		if (capability == ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		return null;
	}

	public float GetTemp()
	{
		return heatUser.GetTemperature();
	}

	public float GetBurnPercent()
	{
		return burnTime / (float) maxBurnTime;
	}

	public float GetTempPercent()
	{
		return heatUser.GetTemperature() / 2500f;
	}

	@Override
	public void readFromNBT(CompoundNBT tag)
	{
		burnTime = tag.getInteger("burnTime");
		maxBurnTime = tag.getInteger("maxBurnTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");

		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		heatUser.ReadFromNBT(tag.getCompoundTag("heatUser"));

		super.readFromNBT(tag);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag)
	{
		tag.setInteger("burnTime", burnTime);
		tag.setInteger("maxBurnTime", maxBurnTime);
		tag.setBoolean("isBurningFuel", isBurningFuel);

		tag.setTag("inventory", inventory.serializeNBT());
		tag.setTag("heatUser", heatUser.WriteToNBT());

		return super.writeToNBT(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
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

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		readFromNBT(tag);
	}
}
