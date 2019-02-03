package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.MetalOres;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static boblovespi.factoryautomation.common.block.processing.StoneCrucible.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/28/2018.
 */
public class TEStoneCrucible extends TileEntity implements IMultiblockControllerTE, ITickable
{
	public static final String MULTIBLOCK_ID = "stone_foundry";
	private static final List<MetalInfo> infos = new ArrayList<MetalInfo>(20)
	{{
		add(new MetalInfo(new OreIngredient("oreCopper"), "copper", 18));
		add(new MetalInfo(new OreIngredient("nuggetCopper"), "copper", 2));
		add(new MetalInfo(new OreIngredient("ingotCopper"), "copper", 18));
		add(new MetalInfo(new OreIngredient("stickCopper"), "copper", 9));
		add(new MetalInfo(new OreIngredient("plateCopper"), "copper", 18));
		add(new MetalInfo(new OreIngredient("blockCopper"), "copper", 162));
		add(new MetalInfo(new OreIngredient("oreTin"), "tin", 18));
		add(new MetalInfo(new OreIngredient("nuggetTin"), "tin", 2));
		add(new MetalInfo(new OreIngredient("ingotTin"), "tin", 18));
		add(new MetalInfo(new OreIngredient("stickTin"), "tin", 9));
		add(new MetalInfo(new OreIngredient("plateTin"), "tin", 18));
		add(new MetalInfo(new OreIngredient("blockTin"), "tin", 162));
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

	public TEStoneCrucible()
	{
		metals = new MetalHelper(MetalForms.INGOT.amount * 9 * 3, 1.5f);
		inventory = new ItemStackHandler(2);
		heatUser = new HeatUser(20, 1000, 300);
	}

	public static String GetMetalFromStack(ItemStack stack)
	{
		Item item = stack.getItem();

		// refined variants
		if (item == Items.IRON_INGOT || item == Items.IRON_NUGGET || item == Item.getItemFromBlock(Blocks.IRON_BLOCK))
			return "iron";
		if (item == Items.GOLD_INGOT || item == Items.GOLD_NUGGET || item == Item.getItemFromBlock(Blocks.GOLD_BLOCK))
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
		if (item == Item.getItemFromBlock(Blocks.GOLD_ORE))
			return "gold";
		if (item == Item.getItemFromBlock(FABlocks.metalOres.GetBlock(MetalOres.COPPER)))
			return "copper";
		if (item == Item.getItemFromBlock(FABlocks.metalOres.GetBlock(MetalOres.TIN)))
			return "tin";

		// iron shards
		if (item == FAItems.ironShard)
			return "iron";

		// oredict
		for (MetalInfo info : infos)
		{
			if (info.ore.apply(stack))
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
			if (info.ore.apply(stack))
				return info.amount * mult;
		}

		return 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
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
				fuelInfo = FuelRegistry.GetInfo(burnStack.getItem(), burnStack.getItemDamage());
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
				fuelInfo = FuelRegistry.GetInfo(burnStack.getItem(), burnStack.getItemDamage());
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
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
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
		MultiblockHelper
				.CreateStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(StoneCrucible.FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, true));
		structureIsValid = true;
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper
				.BreakStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(StoneCrucible.FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, false));
		structureIsValid = false;
	}

	@Override
	public <T> T GetCapability(Capability<T> capability, int[] offset, EnumFacing side)
	{
		return null;
	}

	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !(oldState.getBlock() == FABlocks.stoneCrucible && newState.getBlock() == FABlocks.stoneCrucible);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		metals.ReadFromNBT(tag.getCompoundTag("metals"));
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		heatUser.ReadFromNBT(tag.getCompoundTag("heatUser"));
		structureIsValid = tag.getBoolean("structureIsValid");
		burnTime = tag.getInteger("burnTime");
		maxBurnTime = tag.getInteger("maxBurnTime");
		meltTime = tag.getInteger("meltTime");
		isBurningFuel = tag.getBoolean("isBurningFuel");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setTag("metals", metals.WriteToNBT());
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setTag("heatUser", heatUser.WriteToNBT());
		tag.setBoolean("structureIsValid", structureIsValid);
		tag.setInteger("burnTime", burnTime);
		tag.setInteger("maxBurnTime", maxBurnTime);
		tag.setInteger("meltTime", meltTime);
		tag.setBoolean("isBurningFuel", isBurningFuel);
		return super.writeToNBT(tag);
	}

	@SuppressWarnings("MethodCallSideOnly")
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

	public void PourInto(EnumFacing facing)
	{
		TileEntity te1 = world.getTileEntity(pos.down().offset(facing));
		if (te1 instanceof TEStoneCastingVessel)
		{
			TEStoneCastingVessel te = (TEStoneCastingVessel) te1;
			MetalForms form = te.GetForm();
			if (!(form == MetalForms.NONE) && te.HasSpace())
			{
				te.CastInto(metals.CastItem(form), Metals.GetFromName(metals.metal).meltTemp);
			}
		}
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
		private final int maxCapacity;
		private final float wasteFactor;
		private String metal = "none";
		private int amount = 0;

		public MetalHelper(int maxCapacity, float wasteFactor)
		{
			this.maxCapacity = maxCapacity;
			this.wasteFactor = wasteFactor;
		}

		public ItemStack CastItem(MetalForms form)
		{
			if (metal.equals("none") || amount == 0)
				return ItemStack.EMPTY;
			float actualWaste = form == MetalForms.INGOT || form == MetalForms.NUGGET ? 1 : wasteFactor;
			int left = Math.max(0, amount - (int) (form.amount * actualWaste));
			int toDrain = amount - left;
			amount = left;
			String drainMetal = metal;
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

		public NBTTagCompound WriteToNBT()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("metal", metal);
			tag.setInteger("amount", amount);
			return tag;
		}

		public void ReadFromNBT(NBTTagCompound tag)
		{
			metal = tag.getString("metal");
			amount = tag.getShort("amount");
		}

	}

	private static class MetalInfo
	{
		public OreIngredient ore;
		public String metal;
		public int amount;

		public MetalInfo(OreIngredient ore, String metal, int amount)
		{
			this.ore = ore;
			this.metal = metal;
			this.amount = amount;
		}
	}
}
