package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 4/8/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FAItemGroups
{
	public static CreativeModeTab metallurgy = new CreativeModeTab("tabs.metallurgy.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.clayCrucible);
		}

	};
	public static CreativeModeTab resources = new CreativeModeTab("tabs.resources.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.diamondGravel);
		}
	};
	public static CreativeModeTab tools = new CreativeModeTab("tabs.tools.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.ironHammer);
		}
	};
	public static CreativeModeTab mechanical = new CreativeModeTab("tabs.mechanical.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.gear.GetItem(Gearbox.GearType.STEEL));
		}
	};
	public static CreativeModeTab crafting = new CreativeModeTab("tabs.crafting.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.glassLens);
		}
	};
	public static CreativeModeTab electrical = new CreativeModeTab("tabs.electrical.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FAItems.copperWire);
		}
	};
	public static CreativeModeTab heat = new CreativeModeTab("tabs.heat.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(Blocks.FURNACE);
		}
	};
	public static CreativeModeTab primitive = new CreativeModeTab("tabs.primitive.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(FABlocks.campfire);
		}
	};
	public static CreativeModeTab fluid = new CreativeModeTab("tabs.fluid.name")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(Items.WATER_BUCKET);
		}
	};
}
