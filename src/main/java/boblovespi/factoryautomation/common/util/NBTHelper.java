package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.energy.EnergyConnection;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Created by Willi on 11/26/2017.
 */
public class NBTHelper
{
	public static MultiblockStructurePattern GetStructurePattern(NBTTagCompound compound, String key)
	{
		return MultiblockHandler.Get(compound.getString(key));
	}

	public static NBTTagCompound GetTag(ItemStack items)
	{
		if (!items.hasTagCompound())
		{
			items.setTagCompound(new NBTTagCompound());
		}
		return items.getTagCompound();
	}

	public static boolean HasKey(ItemStack stack, String key)
	{
		return HasTag(stack) && GetTag(stack).hasKey(key);
	}

	public static boolean HasTag(ItemStack stack)
	{
		return stack.hasTagCompound();
	}

	public static void SetLocationTag(ItemStack stack, String key, int dim,
			int x, int y, int z)
	{
		GetTag(stack).setIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(ItemStack stack,
			String key)
	{
		int[] locs = GetTag(stack).getIntArray(key);
		if (locs[0] != DimLocation.CLASS_KEY && locs.length != 5)
			return null;

		return new DimLocation(locs[1], locs[2], locs[3], locs[4]);
	}

	public static boolean GetBoolTag(ItemStack stack, String key)
	{
		return GetTag(stack).getBoolean("key");
	}

	public static void SetBoolTag(ItemStack stack, String key, boolean val)
	{
		GetTag(stack).setBoolean(key, val);
	}

	public static void ToggleBoolTag(ItemStack stack, String key)
	{
		SetBoolTag(stack, key, !GetBoolTag(stack, key));
	}

	public static void SetLocationTag(NBTTagCompound stack, String key, int dim,
			int x, int y, int z)
	{
		stack.setIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(NBTTagCompound stack,
			String key)
	{
		int[] locs = stack.getIntArray(key);
		if (locs[0] != DimLocation.CLASS_KEY && locs.length != 5)
			return null;

		return new DimLocation(locs[1], locs[2], locs[3], locs[4]);
	}

	public static EnergyConnection GetEnergyConnection(NBTTagCompound tag)
	{
		EnergyConnection connection = new EnergyConnection();
		return null;
	}
}