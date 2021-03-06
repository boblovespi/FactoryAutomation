package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.container.ContainerStoneFoundry;
import boblovespi.factoryautomation.common.container.StringIntArray;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.common.block.processing.StoneCrucible.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/28/2018.
 */
public class TEStoneCrucible extends TileEntity
		implements IMultiblockControllerTE, ITickableTileEntity, INamedContainerProvider
{
	public static final String MULTIBLOCK_ID = "stone_foundry";
	public static final List<MetalInfo> infos = new ArrayList<MetalInfo>(20)
	{{
		add(new MetalInfo("ores/iron", "iron", 18));
		add(new MetalInfo("nuggets/iron", "iron", 2));
		add(new MetalInfo("ingots/iron", "iron", 18));
		add(new MetalInfo("sticks/iron", "iron", 9));
		add(new MetalInfo("plates/iron", "iron", 18));
		add(new MetalInfo("storage_blocks/iron", "iron", 162));
		add(new MetalInfo("ores/gold", "gold", 18));
		add(new MetalInfo("nuggets/gold", "gold", 2));
		add(new MetalInfo("ingots/gold", "gold", 18));
		add(new MetalInfo("sticks/gold", "gold", 9));
		add(new MetalInfo("plates/gold", "gold", 18));
		add(new MetalInfo("storage_blocks/gold", "gold", 162));
		add(new MetalInfo("ores/copper", "copper", 18));
		add(new MetalInfo("nuggets/copper", "copper", 2));
		add(new MetalInfo("ingots/copper", "copper", 18));
		add(new MetalInfo("sticks/copper", "copper", 9));
		add(new MetalInfo("plates/copper", "copper", 18));
		add(new MetalInfo("storage_blocks/copper", "copper", 162));
		add(new MetalInfo("ores/tin", "tin", 18));
		add(new MetalInfo("nuggets/tin", "tin", 2));
		add(new MetalInfo("ingots/tin", "tin", 18));
		add(new MetalInfo("sticks/tin", "tin", 9));
		add(new MetalInfo("plates/tin", "tin", 18));
		add(new MetalInfo("storage_blocks/tin", "tin", 162));
	}};
	private MetalHelper metals;
	private ItemStackHandler inventory;
	private HeatUser heatUser;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private int meltTime = 0;
	private int maxMeltTime = 200;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;
	private boolean structureIsValid = false;
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
				return (int) (GetMeltPercent() * 100);
			case 3:
				return (int) (GetCapacityPercent() * 100);
			case 4:
				return GetColor();
			case 5:
				return (int) (GetTemp() * 10);
			case 6:
				return GetAmountMetal();
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
			return 7;
		}
	};
	private StringIntArray metalName;

	public TEStoneCrucible()
	{
		super(TileEntityHandler.teStoneCrucible);
		metals = new MetalHelper(MetalForms.INGOT.amount * 9 * 3, 1.5f);
		inventory = new ItemStackHandler(2);
		heatUser = new HeatUser(20, 1000, 300);
		metalName = new StringIntArray(8);
		metalName.SetSource(this::GetMetalName);
	}

	public static String GetMetalFromStack(ItemStack stack)
	{
		Item item = stack.getItem();

		// refined variants
		if (item == Items.IRON_INGOT || item == Items.IRON_NUGGET || item == Items.IRON_BLOCK)
			return "iron";
		if (item == Items.GOLD_INGOT || item == Items.GOLD_NUGGET || item == Items.GOLD_BLOCK)
			return "gold";
		for (Metals metal : Metals.values())
		{
			if (metal != Metals.IRON && metal != Metals.GOLD)
			{
				if (item == FAItems.ingot.GetItem(metal) || item == FAItems.nugget.GetItem(metal)
						|| item == FAItems.sheet.GetItem(metal) || item == FAItems.rod.GetItem(metal)
						|| item == FABlocks.metalBlock.GetBlock(metal).GetItem())
					return metal.getName();
			} else if (item == FAItems.sheet.GetItem(metal) || item == FAItems.rod.GetItem(metal)
					|| item == FABlocks.metalBlock.GetBlock(metal).GetItem())
				return metal.getName();
		}

		// ores and other raw forms
		if (item == Items.GOLD_ORE)
			return "gold";
		if (item == Item.getItemFromBlock(FABlocks.metalOres.GetBlock(MetalOres.COPPER)))
			return "copper";
		if (item == Item.getItemFromBlock(FABlocks.metalOres.GetBlock(MetalOres.TIN)))
			return "tin";

		// iron shards
		if (item == FAItems.ironShard)
			return "iron";

		// tags now (formerly oredict)
		for (MetalInfo info : infos)
		{
			if (FATags.ForgeItemTag(info.ore).contains(item))
				return info.metal;
		}
		return "none";
	}

	public static int GetAmountFromStack(ItemStack stack)
	{
		int mult = stack.getCount();
		Item item = stack.getItem();

		// refined forms
		if (FAItems.ingot.Contains(item))
			return mult * 18;
		if (FAItems.nugget.Contains(item))
			return mult * 2;
		if (FAItems.rod.Contains(item))
			return mult * 9;
		if (FAItems.sheet.Contains(item))
			return mult * 18;
		for (int i = 2; i < Metals.values().length; i++)
		{
			if (item == Item.getItemFromBlock(FABlocks.metalBlock.GetBlock(Metals.values()[i])))
				return mult * 18 * 9;
		}

		// ores and other raw forms
		if (item == Item.getItemFromBlock(Blocks.GOLD_ORE))
			return mult * 18;
		for (MetalOres ore : MetalOres.values())
		{
			if (item == Item.getItemFromBlock(FABlocks.metalOres.GetBlock(ore)))
				return mult * 18;
		}

		// iron shards
		if (item == FAItems.ironShard)
			return mult * 2;

		// oredict
		for (MetalInfo info : infos)
		{
			if (FATags.ForgeItemTag(info.ore).contains(item))
				return info.amount * mult;
		}
		return 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tick()
	{
		if (world.isRemote || !IsStructureValid())
			return;
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
			String metal = GetMetalFromStack(meltStack);
			int amount = GetAmountFromStack(meltStack);
			if (metal.equals("none"))
			{
				meltTime = 0;
			} else if (metal.equals(metals.metal) || metals.metal.equals("none"))
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
		if (heatUser.GetTemperature() > 1800)
			heatUser.SetTemperature(1800);

		markDirty();

		/* IMPORTANT */
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
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
		MultiblockHelper.CreateStructure(world, pos, MULTIBLOCK_ID, getBlockState().get(StoneCrucible.FACING));
		world.setBlockState(pos, getBlockState().with(MULTIBLOCK_COMPLETE, true));
		structureIsValid = true;
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper.BreakStructure(world, pos, MULTIBLOCK_ID, getBlockState().get(StoneCrucible.FACING));
		world.setBlockState(pos, getBlockState().with(MULTIBLOCK_COMPLETE, false));
		structureIsValid = false;
	}

	@Override
	public <T> LazyOptional<T> GetCapability(Capability<T> capability, int[] offset, Direction side)
	{
		return LazyOptional.empty();
	}

	@Override
	public void read(CompoundNBT tag)
	{
		super.read(tag);
		metals.ReadFromNBT(tag.getCompound("metals"));
		inventory.deserializeNBT(tag.getCompound("inventory"));
		heatUser.ReadFromNBT(tag.getCompound("heatUser"));
		structureIsValid = tag.getBoolean("structureIsValid");
		burnTime = tag.getInt("burnTime");
		maxBurnTime = tag.getInt("maxBurnTime");
		meltTime = tag.getInt("meltTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.put("metals", metals.WriteToNBT());
		tag.put("inventory", inventory.serializeNBT());
		tag.put("heatUser", heatUser.WriteToNBT());
		tag.putBoolean("structureIsValid", structureIsValid);
		tag.putInt("burnTime", burnTime);
		tag.putInt("maxBurnTime", maxBurnTime);
		tag.putInt("meltTime", meltTime);
		tag.putBoolean("isBurningFuel", isBurningFuel);
		return super.write(tag);
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
		TileEntity te1 = world.getTileEntity(pos.down().offset(facing));
		if (te1 instanceof ICastingVessel)
		{
			ICastingVessel te = (ICastingVessel) te1;
			MetalForms form = te.GetForm();
			if (!(form == MetalForms.NONE) && te.HasSpace())
			{
				StringBuilder metalOut = new StringBuilder();
				te.CastInto(metals.CastItem(form, metalOut), Metals.GetFromName(metalOut.toString()).meltTemp);
			}
		}
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
		return new ContainerStoneFoundry(id, playerInv, inventory, containerInfo, metalName, pos);
	}

	public enum MetalForms
	{
		INGOT(18), NUGGET(2), SHEET(18), ROD(9), GEAR(72), COIN(2), NONE(0);
		public final int amount;

		MetalForms(int amount)
		{
			this.amount = amount;
		}
	}

	public static class MetalHelper
	{
		final int maxCapacity;
		private final float wasteFactor;
		String metal = "none";
		int amount = 0;

		public MetalHelper(int maxCapacity, float wasteFactor)
		{
			this.maxCapacity = maxCapacity;
			this.wasteFactor = wasteFactor;
		}

		public ItemStack CastItem(MetalForms form, StringBuilder metalOut)
		{
			if (metal.equals("none") || amount == 0)
				return ItemStack.EMPTY;
			float actualWaste = form == MetalForms.INGOT || form == MetalForms.NUGGET ? 1 : wasteFactor;
			int left = Math.max(0, amount - (int) (form.amount * actualWaste));
			int toDrain = amount - left;
			amount = left;
			String drainMetal = metal;
			metalOut.append(drainMetal);
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

		public int AddMetal(String metalToAdd, int amountToAdd)
		{
			if ((metal.equals("none") || amount == 0) && amountToAdd > 0)
			{
				amount = Math.min(maxCapacity, amountToAdd);
				metal = metalToAdd;
				return amountToAdd - amount;
			} else if (metal.equals(metalToAdd))
			{
				amount = Math.min(maxCapacity, amountToAdd + amount);
				return amountToAdd - amount;
			} else
				return amountToAdd;
		}

		public CompoundNBT WriteToNBT()
		{
			CompoundNBT tag = new CompoundNBT();
			tag.putString("metal", metal);
			tag.putInt("amount", amount);
			return tag;
		}

		public void ReadFromNBT(CompoundNBT tag)
		{
			metal = tag.getString("metal");
			amount = tag.getShort("amount");
		}

	}

	static class MetalInfo
	{
		public String ore;
		public String metal;
		public int amount;

		public MetalInfo(String ore, String metal, int amount)
		{
			this.ore = ore;
			this.metal = metal;
			this.amount = amount;
		}
	}
}
