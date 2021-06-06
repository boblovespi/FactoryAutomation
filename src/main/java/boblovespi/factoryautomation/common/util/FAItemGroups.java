package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.item.FAItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 4/8/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FAItemGroups
{
	public static ItemGroup metallurgy = new ItemGroup("tabs.metallurgy.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.clayCrucible.toItem());
		}

	};
	public static ItemGroup resources = new ItemGroup("tabs.resources.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.diamondGravel.toItem());
		}
	};
	public static ItemGroup tools = new ItemGroup("tabs.tools.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.ironHammer.toItem());
		}
	};
	public static ItemGroup mechanical = new ItemGroup("tabs.mechanical.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.gear.GetItem(Gearbox.GearType.STEEL));
		}
	};
	public static ItemGroup crafting = new ItemGroup("tabs.crafting.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.glassLens.toItem());
		}
	};
	public static ItemGroup electrical = new ItemGroup("tabs.electrical.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FAItems.copperWire.toItem());
		}
	};
	public static ItemGroup heat = new ItemGroup("tabs.heat.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(Blocks.FURNACE);
		}
	};
	public static ItemGroup primitive = new ItemGroup("tabs.primitive.name")
	{
		@Override
		public ItemStack getIcon()
		{
			return new ItemStack(FABlocks.campfire.toBlock());
		}
	};
}
