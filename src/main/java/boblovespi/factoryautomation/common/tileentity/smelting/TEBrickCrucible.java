package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.api.misc.BellowsUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.container.ContainerBrickFoundry;
import boblovespi.factoryautomation.common.container.StringIntArray;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.NBTHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static boblovespi.factoryautomation.common.block.processing.StoneCrucible.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/28/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class TEBrickCrucible extends TileEntity
		implements IMultiblockControllerTE, ITickableTileEntity, INamedContainerProvider
{
	public static final String MULTIBLOCK_ID = "brick_foundry";
	private final MultiMetalHelper metals;
	private final ItemStackHandler inventory;
	private final HeatUser heatUser;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private int meltTime = 0;
	private final int maxMeltTime = 200;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;
	private boolean structureIsValid = false;
	private final BellowsUser bellowsUser;
	private final IIntArray containerInfo = new IIntArray()
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
				return (int) (GetMeltPercent() * 100);
			case 3:
				return (int) (GetBellowsPercent() * 100);
			case 4:
				return (int) (GetCapacityPercent() * 100);
			case 5:
				return GetColor();
			case 6:
				return (int) (GetTemp() * 10);
			case 7:
				return GetAmountMetal();
			case 8:
				return (int) (GetEfficiencyPercent() * 100);
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
			return 9;
		}
	};
	private final StringIntArray metalName;

	public TEBrickCrucible()
	{
		super(TileEntityHandler.teBrickCrucible);
		metals = new MultiMetalHelper(TEStoneCrucible.MetalForms.INGOT.amount * 9 * 3, 1.5f);
		inventory = new ItemStackHandler(2);
		heatUser = new HeatUser(20, 1000, 300);
		bellowsUser = new BellowsUser(0.5f);
		metalName = new StringIntArray(8);
		metalName.SetSource(this::GetMetalName);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (Objects.requireNonNull(level).isClientSide || !IsStructureValid())
			return;
		bellowsUser.Tick();
		TEHelper.DissipateHeat(heatUser, 6);
		ItemStack burnStack = inventory.getStackInSlot(0);
		ItemStack meltStack = inventory.getStackInSlot(1);
		if (isBurningFuel)
		{
			burnTime--;
			if (fuelInfo == FuelRegistry.NULL)
			{
				fuelInfo = FuelRegistry.GetInfo(burnStack.getItem());
				if (fuelInfo == FuelRegistry.NULL)
				{
					burnTime = 0;
					maxBurnTime = 1;
					isBurningFuel = false;
				}
			}
			if (fuelInfo != FuelRegistry.NULL && heatUser.GetTemperature() <= fuelInfo.GetBurnTemp() * bellowsUser
					.GetEfficiency())
			{
				heatUser.TransferEnergy(
						fuelInfo.GetTotalEnergy() / (float) fuelInfo.GetBurnTime() * bellowsUser.GetEfficiency());
			}
			if (burnTime <= 0)
			{
				burnTime = 0;
				maxBurnTime = 1;
				isBurningFuel = false;
			}
		} else
		{
			if (!burnStack.isEmpty())
			{
				fuelInfo = FuelRegistry.GetInfo(burnStack.getItem());
				if (fuelInfo != FuelRegistry.NULL)
				{
					burnTime = fuelInfo.GetBurnTime();
					maxBurnTime = fuelInfo.GetBurnTime();
					isBurningFuel = true;
					inventory.extractItem(0, 1, false);
				}
			}
		}
		if (!meltStack.isEmpty())
		{
			float temp = heatUser.GetTemperature();
			String metal = TEStoneCrucible.GetMetalFromStack(meltStack);
			int amount = TEStoneCrucible.GetAmountFromStack(meltStack);
			if (metal.equals("none"))
			{
				meltTime = 0;
			} else
			{
				int meltTemp = Metals.GetFromName(metal).meltTemp;
				if (temp >= meltTemp)
					meltTime++;
				else
					meltTime -= 2;
				if (meltTime > maxMeltTime)
				{
					metals.AddMetal(metal, amount);
					meltTime = 0;
					inventory.setStackInSlot(1, ItemStack.EMPTY);
				}
			}
		} else
			meltTime = 0;
		if (meltTime < 0)
			meltTime = 0;
		if (heatUser.GetTemperature() > 2300)
			heatUser.SetTemperature(2300);

		setChanged();

		/* IMPORTANT */
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	@Override
	public void SetStructureValid(boolean isValid)
	{
		structureIsValid = isValid;
	}

	@Override
	public boolean IsStructureValid()
	{
		return structureIsValid;
	}

	@Override
	public void CreateStructure()
	{
		MultiblockHelper.CreateStructure(level, worldPosition, MULTIBLOCK_ID, getBlockState().getValue(StoneCrucible.FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(MULTIBLOCK_COMPLETE, true));
		structureIsValid = true;
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(level, worldPosition, MULTIBLOCK_ID, getBlockState().getValue(StoneCrucible.FACING));
		Objects.requireNonNull(level).setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(MULTIBLOCK_COMPLETE, false));
		structureIsValid = false;
	}

	@Nonnull
    @Override
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0
				&& capability == CapabilityBellowsUser.BELLOWS_USER_CAPABILITY)
			return LazyOptional.of(() -> (T) bellowsUser);
		return LazyOptional.empty();
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		super.load(state, tag);
		metals.ReadFromNBT(tag.getCompound("metals"));
		inventory.deserializeNBT(tag.getCompound("inventory"));
		heatUser.ReadFromNBT(tag.getCompound("heatUser"));
		structureIsValid = tag.getBoolean("structureIsValid");
		burnTime = tag.getInt("burnTime");
		maxBurnTime = tag.getInt("maxBurnTime");
		meltTime = tag.getInt("meltTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");
		bellowsUser.ReadFromNBT(tag.getCompound("bellowsUser"));
	}

	@Override
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.put("metals", metals.WriteToNBT());
		tag.put("inventory", inventory.serializeNBT());
		tag.put("heatUser", heatUser.WriteToNBT());
		tag.putBoolean("structureIsValid", structureIsValid);
		tag.putInt("burnTime", burnTime);
		tag.putInt("maxBurnTime", maxBurnTime);
		tag.putInt("meltTime", meltTime);
		tag.putBoolean("isBurningFuel", isBurningFuel);
		tag.put("bellowsUser", bellowsUser.WriteToNBT());
		return super.save(tag);
	}

	public IItemHandler GetInventory()
	{
		return inventory;
	}

	public float GetBurnPercent()
	{
		return (float) burnTime / (float) maxBurnTime;
	}

	public float GetTempPercent()
	{
		return heatUser.GetTemperature() / 1800f;
	}

	public float GetTemp()
	{
		return heatUser.GetTemperature();
	}

	public float GetMeltPercent()
	{
		return (float) meltTime / (float) maxMeltTime;
	}

	public int GetColor()
	{
		if (metals.metal.equals("none"))
			return 0;
		if (metals.metal.equals("unknown"))
			return 0xFF453A2B;
		return Metals.GetFromName(metals.metal).color;
	}

	public float GetCapacityPercent()
	{
		return metals.amount / (float) metals.maxCapacity;
	}

	public String GetMetalName()
	{
		if (metals.metal.equals("none"))
			return "";
		return "metal" + metals.metal + "name";
	}

	public int GetAmountMetal()
	{
		return metals.amount;
	}

	public void PourInto(Direction facing)
	{
		TileEntity te1 = Objects.requireNonNull(level).getBlockEntity(worldPosition.below().relative(facing));
		if (te1 instanceof ICastingVessel)
		{
			ICastingVessel te = (ICastingVessel) te1;
			TEStoneCrucible.MetalForms form = te.GetForm();
			if (!(form == TEStoneCrucible.MetalForms.NONE) && te.HasSpace())
			{
				StringBuilder metalOut = new StringBuilder();
				ItemStack item = metals.CastItem(form, metalOut);
				te.CastInto(item, Metals.GetFromName(metalOut.toString()).meltTemp);
			}
		}
	}

	public float GetEfficiencyPercent()
	{
		return bellowsUser.GetEfficiency() * 100f;
	}

	public float GetBellowsPercent()
	{
		return bellowsUser.GetTime() * 1f / bellowsUser.GetMaxTime();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("");
	}

	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return new ContainerBrickFoundry(id, playerInv, inventory, containerInfo, metalName, worldPosition);
	}

	private class MultiMetalHelper
	{
		final int maxCapacity;
		final float wasteFactor;
		String metal = "none";
		int amount = 0;
		Map<String, Float> metals;

		public MultiMetalHelper(int maxCapacity, float wasteFactor)
		{
			this.maxCapacity = maxCapacity;
			this.wasteFactor = wasteFactor;
			metals = new HashMap<>();
		}

		private void RecalculateMetal()
		{
			metals.values().removeIf(n -> n < 0.1f);
			// not an alloy:
			if (metals.size() == 1)
			{
				metal = metals.keySet().iterator().next();
				return;
			}
			// bronze:
			if (metals.size() == 2)
			{
				if (metals.containsKey(Metals.COPPER.getSerializedName()) && metals.containsKey(Metals.TIN.getSerializedName()))
				{
					if (metals.get(Metals.COPPER.getSerializedName()) / metals.get(Metals.TIN.getSerializedName()) >= 7
							&& metals.get(Metals.COPPER.getSerializedName()) / metals.get(Metals.TIN.getSerializedName()) <= 9
							&& heatUser.GetTemperature() > 2000)
					{
						metal = "bronze";
						return;
					}
				}
			}
			// bronze (again):
			if (metals.size() == 3)
			{
				if (metals.containsKey(Metals.COPPER.getSerializedName()) && metals.containsKey(Metals.TIN.getSerializedName()) && metals
						.containsKey(Metals.BRONZE.getSerializedName()))
				{
					if (metals.get(Metals.COPPER.getSerializedName()) / metals.get(Metals.TIN.getSerializedName()) >= 7
							&& metals.get(Metals.COPPER.getSerializedName()) / metals.get(Metals.TIN.getSerializedName()) <= 9
							&& heatUser.GetTemperature() > 2000)
					{
						metal = "bronze";
						return;
					}
				}
			}
			metal = "unknown";
		}

		public int AddMetal(String metalToAdd, int amountToAdd)
		{
			amountToAdd = Math.min(maxCapacity - amount, amountToAdd);
			amount += amountToAdd;
			if (metals.containsKey(metalToAdd))
				metals.put(metalToAdd, metals.get(metalToAdd) + amountToAdd);
			else
				metals.put(metalToAdd, (float) amountToAdd);

			RecalculateMetal();
			return amountToAdd - amount;
		}

		public void ReadFromNBT(CompoundNBT tag)
		{
			metal = tag.getString("metal");
			amount = tag.getShort("amount");
			metals = NBTHelper.GetMap(tag, "metals", k -> k, t -> ((FloatNBT) t).getAsFloat());
		}

		public CompoundNBT WriteToNBT()
		{
			CompoundNBT tag = new CompoundNBT();
			tag.putString("metal", metal);
			tag.putInt("amount", amount);
			NBTHelper.SetMap(tag, "metals", metals, k -> k, FloatNBT::valueOf);
			return tag;
		}

		public ItemStack CastItem(TEStoneCrucible.MetalForms form, StringBuilder metalOut)
		{
			if (metal.equals("none") || amount == 0 || metal.equals("unknown"))
				return ItemStack.EMPTY;
			float actualWaste =
					form == TEStoneCrucible.MetalForms.INGOT || form == TEStoneCrucible.MetalForms.NUGGET ? 1 :
							wasteFactor;
			int left = Math.max(0, amount - (int) (form.amount * actualWaste));
			int toDrain = amount - left;
			float mult = left / (float) amount;
			amount = left;

			// update map
			for (Map.Entry<String, Float> entry : metals.entrySet())
			{
				entry.setValue(entry.getValue() * mult);
			}
			String drainMetal = metal;
			metalOut.append(drainMetal);

			RecalculateMetal();

			if (amount == 0)
				metal = "none";
			if (toDrain >= (int) (form.amount * actualWaste - 1))
				switch (form)
				{
				case INGOT:
					if (drainMetal.equals("iron"))
						return new ItemStack(Items.IRON_INGOT);
					else if (drainMetal.equals("gold"))
						return new ItemStack(Items.GOLD_INGOT);
					return new ItemStack(FAItems.ingot.GetItem(Metals.GetFromName(drainMetal)));
				case NUGGET:
					if (drainMetal.equals("iron"))
						return new ItemStack(Items.IRON_NUGGET);
					else if (drainMetal.equals("gold"))
						return new ItemStack(Items.GOLD_NUGGET);
					return new ItemStack(FAItems.nugget.GetItem(Metals.GetFromName(drainMetal)));
				case SHEET:
					return new ItemStack(FAItems.sheet.GetItem(Metals.GetFromName(drainMetal)));
				case ROD:
					return new ItemStack(FAItems.rod.GetItem(Metals.GetFromName(drainMetal)));
				case GEAR:
					if (drainMetal.equals("pig_iron") || drainMetal.equals("lead") || drainMetal.equals("silver"))
						return ItemStack.EMPTY;
					return new ItemStack(FAItems.gear.GetItem(Gearbox.GearType.valueOf(drainMetal.toUpperCase())));
				case COIN:
					return new ItemStack(FAItems.coin.GetItem(Metals.GetFromName(drainMetal)));
				}
			return ItemStack.EMPTY;
		}
	}
}
