package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static boblovespi.factoryautomation.common.util.SetBlockStateFlags.UPDATE_NO_RERENDER;

public class TEBrickCastingVessel extends BlockEntity implements ITickable, ICastingVessel
{
	private final FluidTank tank;
	private final ItemStackHandler items; // 0 is the mold, 1 is the metal
	private int effectsCounter = 40; // every 40 ticks play steam + sfx if necessary
	private float temp;
	private TEStoneCrucible.MetalForms form;

	public TEBrickCastingVessel(BlockPos pos, BlockState state)
	{
		super(TileEntityHandler.teBrickCastingVessel.get(), pos, state);
		tank = new FluidTank(500, f -> f.getFluid().getFluidType() == ForgeMod.WATER_TYPE.get())
		{
			@Override
			protected void onContentsChanged()
			{
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), UPDATE_NO_RERENDER);
			}
		};
		items = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), UPDATE_NO_RERENDER);
			}
		};
		temp = 20f;
		form = TEStoneCrucible.MetalForms.NONE;
	}

	@Override
	public void CastInto(ItemStack stack, int temp)
	{
		items.setStackInSlot(1, stack.copy());
		DamageCast();
		this.temp = temp;
		SpawnEffects();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), UPDATE_NO_RERENDER);
	}

	@Override
	public float GetLoss()
	{
		return 4f / 3f;
	}

	@Override
	public boolean HasSpace()
	{
		return items.getStackInSlot(1).isEmpty();
	}

	@Override
	public TEStoneCrucible.MetalForms GetForm()
	{
		return form;
	}

	@Override
	public void tick()
	{
		if (level.isClientSide)
			return;
		if (temp > 20f)
		{
			if (!tank.isEmpty())
			{
				// use at max 10mB/tick (200mB/s)
				var drained = tank.drain(10, IFluidHandler.FluidAction.EXECUTE).getAmount();
				for (int i = 0; i < drained; i++)
					temp *= 0.99342265857309f; // almost exactly 500mB per cast (or 2.5s)
			}
			else if (level.isRaining())
				temp *= 0.9938f;
			else
				temp *= 0.9972f;
		}
		effectsCounter--;
		if (effectsCounter < 0)
		{
			if (!tank.isEmpty() && temp > 40f) // display effects
				SpawnEffects();
			setChanged();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), UPDATE_NO_RERENDER);
			effectsCounter = 40;
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		tank.readFromNBT(tag.getCompound("tank"));
		items.deserializeNBT(tag.getCompound("items"));
		temp = tag.getFloat("temp");
		form = TEStoneCrucible.MetalForms.values()[tag.getInt("form")];
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		tag.put("tank", tank.writeToNBT(new CompoundTag()));
		tag.put("items", items.serializeNBT());
		tag.putFloat("temp", temp);
		tag.putInt("form", form.ordinal());
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if (cap == ForgeCapabilities.FLUID_HANDLER)
			return LazyOptional.of(() -> tank).cast();
		return LazyOptional.empty();
	}

	public void TakeOrPlace(ItemStack hand, Player player)
	{
		if (!items.getStackInSlot(1).isEmpty()) // we want to take the casted item
		{
			if (temp < 40f)
			{
				ItemStack taken = items.extractItem(1, 64, false);
				ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
			}
			else
			{
				player.hurt(DamageSource.GENERIC, (temp - 40f) / (temp + 100f) * 20f);
				player.displayClientMessage(
						Component.translatable("info.too_hot", String.format("%1$.1f\u00b0C", temp)), true);
			}
		}
		else if (!items.getStackInSlot(0).isEmpty()) // we want to take the mold
		{
			ItemStack taken = items.extractItem(0, 64, false);
			ItemHelper.PutItemsInInventoryOrDrop(player, taken, level);
			form = TEStoneCrucible.MetalForms.NONE;
		}
		else if (hand.is(FATags.FAItemTag("molds")))
		{
			var cast = hand.split(1);
			items.insertItem(0, cast, false);
			if (cast.is(FATags.FAItemTag("molds/ingot")))
				form = TEStoneCrucible.MetalForms.INGOT;
			else if (cast.is(FATags.FAItemTag("molds/nugget")))
				form = TEStoneCrucible.MetalForms.NUGGET;
			else if (cast.is(FATags.FAItemTag("molds/plate")))
				form = TEStoneCrucible.MetalForms.SHEET;
			else if (cast.is(FATags.FAItemTag("molds/rod")))
				form = TEStoneCrucible.MetalForms.ROD;
			else if (cast.is(FATags.FAItemTag("molds/gear")))
				form = TEStoneCrucible.MetalForms.GEAR;
			else if (cast.is(FATags.FAItemTag("molds/coin")))
				form = TEStoneCrucible.MetalForms.COIN;
			else if (cast.is(FATags.FAItemTag("molds/storage_block")))
				form = TEStoneCrucible.MetalForms.BLOCK;
		}
	}

	private void DamageCast()
	{
		ItemHelper.DamageItem(items.getStackInSlot(0));
		if (items.getStackInSlot(0).isEmpty())
		{
			level.playSound(null, worldPosition, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1f, 1f);
			form = TEStoneCrucible.MetalForms.NONE;
		}
	}

	private void SpawnEffects()
	{
		var x = worldPosition.getX();
		var y = worldPosition.getY() + 1;
		var z = worldPosition.getZ();
		var times = Math.random() * 3 + 2;
		for (int i = 0; i < times; i++)
		{
			((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, x + Math.random(), y, z + Math.random(), 0, 0, 1,
					0, 0.02f + Math.random() / 10);
		}
		level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1f);
	}
}
