package boblovespi.factoryautomation.common.tileentity;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.common.container.ContainerSolidFueledFirebox;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

import static boblovespi.factoryautomation.api.energy.heat.CapabilityHeatUser.HEAT_USER_CAPABILITY;
import static boblovespi.factoryautomation.common.tileentity.TileEntityHandler.teSolidFueledFirebox;
import static net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT;
import static net.minecraftforge.common.util.Constants.BlockFlags.NO_RERENDER;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

/**
 * Created by Willi on 10/28/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TESolidFueledFirebox extends BlockEntity implements TickableBlockEntity, MenuProvider
{
	private final HeatUser heatUser;
	private final ItemStackHandler inventory;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;
	private final ContainerData containerInfo = new ContainerData()
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
		public int getCount()
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

		setChanged();

		// TODO: FIGURE OUT UPDATING TEs
		BlockState state = Objects.requireNonNull(level).getBlockState(worldPosition);
		level.sendBlockUpdated(worldPosition, state, state, DEFAULT | NO_RERENDER);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
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
	public void load(BlockState state, CompoundTag tag)
	{
		burnTime = tag.getInt("burnTime");
		maxBurnTime = tag.getInt("maxBurnTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");

		inventory.deserializeNBT(tag.getCompound("inventory"));
		heatUser.ReadFromNBT(tag.getCompound("heatUser"));

		super.load(state, tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		tag.putInt("burnTime", burnTime);
		tag.putInt("maxBurnTime", maxBurnTime);
		tag.putBoolean("isBurningFuel", isBurningFuel);

		tag.put("inventory", inventory.serializeNBT());
		tag.put("heatUser", heatUser.WriteToNBT());

		return super.save(tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		load(Objects.requireNonNull(level).getBlockState(worldPosition), pkt.getTag());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		CompoundTag nbt = new CompoundTag();
		save(nbt);

		return new ClientboundBlockEntityDataPacket(worldPosition, 0, nbt);
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
	{
		return new ContainerSolidFueledFirebox(id, playerInv, inventory, containerInfo, worldPosition);
	}
}
