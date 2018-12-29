package boblovespi.factoryautomation.common.tileentity.processing;

import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.types.Metals;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import static boblovespi.factoryautomation.common.block.processing.StoneCrucible.MULTIBLOCK_COMPLETE;

/**
 * Created by Willi on 12/28/2018.
 */
public class TEStoneCrucible extends TileEntity implements IMultiblockControllerTE
{
	public static final String MULTIBLOCK_ID = "stone_foundry";

	@Override
	public void SetStructureValid(boolean isValid)
	{
	}

	@Override
	public boolean IsStructureValid()
	{
		return false;
	}

	@Override
	public void CreateStructure()
	{
		MultiblockHelper
				.CreateStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(StoneCrucible.FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, true));
	}

	@Override
	public void BreakStructure()
	{
		MultiblockHelper
				.BreakStructure(world, pos, MULTIBLOCK_ID, world.getBlockState(pos).getValue(StoneCrucible.FACING));
		world.setBlockState(pos, world.getBlockState(pos).withProperty(MULTIBLOCK_COMPLETE, false));
	}

	@Override
	public <T> T GetCapability(Capability<T> capability, int[] offset, EnumFacing side)
	{
		return null;
	}

	public enum MetalForms
	{
		INGOT(18), NUGGET(2), SHEET(18), ROD(9), GEAR(72), COIN(2);
		public final int amount;

		MetalForms(int amount)
		{
			this.amount = amount;
		}
	}

	public static class MetalHelper
	{
		private final int maxCapacity;
		private String metal = "none";
		private int amount = 0;

		public MetalHelper(int maxCapacity)
		{
			this.maxCapacity = maxCapacity;
		}

		public ItemStack CastItem(MetalForms form)
		{
			if (metal.equals("none") || amount == 0)
				return ItemStack.EMPTY;

			int left = Math.max(0, amount - form.amount);
			int toDrain = amount - left;
			amount = left;
			String drainMetal = metal;
			if (amount == 0)
				metal = "none";
			if (toDrain >= form.amount)
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
	}
}
