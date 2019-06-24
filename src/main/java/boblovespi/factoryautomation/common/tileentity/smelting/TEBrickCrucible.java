package boblovespi.factoryautomation.common.tileentity.smelting;

import boblovespi.factoryautomation.api.energy.FuelRegistry;
import boblovespi.factoryautomation.api.energy.heat.HeatUser;
import boblovespi.factoryautomation.api.misc.BellowsUser;
import boblovespi.factoryautomation.api.misc.CapabilityBellowsUser;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import boblovespi.factoryautomation.common.util.NBTHelper;
import boblovespi.factoryautomation.common.util.TEHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static boblovespi.factoryautomation.common.block.processing.StoneCrucible.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/28/2018.
 */
public class TEBrickCrucible extends TileEntity implements IMultiblockControllerTE, ITickable
{
	public static final String MULTIBLOCK_ID = "brick_foundry";
	private MultiMetalHelper metals;
	private ItemStackHandler inventory;
	private HeatUser heatUser;
	private int burnTime = 0;
	private int maxBurnTime = 1;
	private int meltTime = 0;
	private int maxMeltTime = 200;
	private FuelRegistry.FuelInfo fuelInfo = FuelRegistry.NULL;
	private boolean isBurningFuel = false;
	private boolean structureIsValid = false;
	private BellowsUser bellowsUser;

	public TEBrickCrucible()
	{
		metals = new MultiMetalHelper(TEStoneCrucible.MetalForms.INGOT.amount * 9 * 3, 1.5f);
		inventory = new ItemStackHandler(2);
		heatUser = new HeatUser(20, 1000, 300);
		bellowsUser = new BellowsUser(0.5f);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if (world.isRemote || !IsStructureValid())
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
				fuelInfo = FuelRegistry.GetInfo(burnStack.getItem(), burnStack.getItemDamage());
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
		if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0
				&& capability == CapabilityBellowsUser.BELLOWS_USER_CAPABILITY)
			return (T) bellowsUser;
		return null;
	}

	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return !(oldState.getBlock() == FABlocks.brickCrucible && newState.getBlock() == FABlocks.brickCrucible);
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
		bellowsUser.ReadFromNBT(tag.getCompoundTag("bellowsUser"));
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
		tag.setTag("bellowsUser", bellowsUser.WriteToNBT());
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

	public void PourInto(EnumFacing facing)
	{
		TileEntity te1 = world.getTileEntity(pos.down().offset(facing));
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
				if (metals.containsKey(Metals.COPPER.getName()) && metals.containsKey(Metals.TIN.getName()))
				{
					if (metals.get(Metals.COPPER.getName()) / metals.get(Metals.TIN.getName()) >= 7
							&& metals.get(Metals.COPPER.getName()) / metals.get(Metals.TIN.getName()) <= 9
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
				if (metals.containsKey(Metals.COPPER.getName()) && metals.containsKey(Metals.TIN.getName()) && metals
						.containsKey(Metals.BRONZE.getName()))
				{
					if (metals.get(Metals.COPPER.getName()) / metals.get(Metals.TIN.getName()) >= 7
							&& metals.get(Metals.COPPER.getName()) / metals.get(Metals.TIN.getName()) <= 9
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

		public void ReadFromNBT(NBTTagCompound tag)
		{
			metal = tag.getString("metal");
			amount = tag.getShort("amount");
			metals = NBTHelper.GetMap(tag, "metals", k -> k, t -> ((NBTTagFloat) t).getFloat());
		}

		public NBTTagCompound WriteToNBT()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("metal", metal);
			tag.setInteger("amount", amount);
			NBTHelper.SetMap(tag, "metals", metals, k -> k, NBTTagFloat::new);
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
