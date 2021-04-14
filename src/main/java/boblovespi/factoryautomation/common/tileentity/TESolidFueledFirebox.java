package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.common.container.ContainerSolidFueledFirebox;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser.HEAT_USER_CAPABILITY;
import static boblovespi.factoryautomation.common.tileentity.TileEntityHandler.teSolidFueledFirebox;
import static net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT;
import static net.minecraftforge.common.util.Constants.BlockFlags.NO_RERENDER;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

/**
 * Created by Willi on 10/28/2018.
 */
public class TESolidFueledFirebox extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	private HeatUser heatUser;
	private ItemStackHandler inventory;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;
	private IIntArray containerInfo = new IIntArray()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return (int) (GetTemp() * 10);
			case 1:
				return (int) (GetBurnPercent() * 100);
			case 2:
				return (int) (GetTempPercent() * 100);
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
			return 3;
		}
	};

	public TESolidFueledFirebox()
	{
		super(teSolidFueledFirebox);
		heatUser = new HeatUser(20, 1600, 300);
		inventory = new ItemStackHandler(1);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
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
				fuelInfo = FuelRegistry.GetInfo(stack.getItem());
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
				fuelInfo = FuelRegistry.GetInfo(stack.getItem());
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

		// TODO: FIGURE OUT UPDATING TEs
		BlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, DEFAULT | NO_RERENDER);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == HEAT_USER_CAPABILITY && facing == Direction.UP)
			return LazyOptional.of(() -> (T) heatUser);
		if (capability == ITEM_HANDLER_CAPABILITY)
			return LazyOptional.of(() -> (T) inventory);
		return super.getCapability(capability, facing);
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
	public void read(BlockState state, CompoundNBT tag)
	{
		burnTime = tag.getInt("burnTime");
		maxBurnTime = tag.getInt("maxBurnTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");

		inventory.deserializeNBT(tag.getCompound("inventory"));
		heatUser.ReadFromNBT(tag.getCompound("heatUser"));

		super.read(state, tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.putInt("burnTime", burnTime);
		tag.putInt("maxBurnTime", maxBurnTime);
		tag.putBoolean("isBurningFuel", isBurningFuel);

		tag.put("inventory", inventory.serializeNBT());
		tag.put("heatUser", heatUser.WriteToNBT());

		return super.write(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(getBlockState(), pkt.getNbtCompound());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);

		return new SUpdateTileEntityPacket(pos, 0, nbt);
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
		return new ContainerSolidFueledFirebox(id, playerInv, inventory, containerInfo, pos);
	}
}
