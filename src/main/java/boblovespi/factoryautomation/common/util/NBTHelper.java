package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Willi on 11/26/2017.
 */
public class NBTHelper
{
	public static MultiblockStructurePattern GetStructurePattern(CompoundNBT compound, String key)
	{
		return MultiblockHandler.Get(compound.getString(key));
	}

	public static CompoundNBT GetTag(ItemStack items)
	{
		if (!items.hasTagCompound())
		{
			items.putTagCompound(new CompoundNBT());
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

	public static void SetLocationTag(ItemStack stack, String key, int dim, int x, int y, int z)
	{
		GetTag(stack).putIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(ItemStack stack, String key)
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
		GetTag(stack).putBoolean(key, val);
	}

	public static void ToggleBoolTag(ItemStack stack, String key)
	{
		SetBoolTag(stack, key, !GetBoolTag(stack, key));
	}

	public static void SetLocationTag(CompoundNBT stack, String key, int dim, int x, int y, int z)
	{
		stack.putIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(CompoundNBT stack, String key)
	{
		int[] locs = stack.getIntArray(key);
		if (locs[0] != DimLocation.CLASS_KEY && locs.length != 5)
			return null;

		return new DimLocation(locs[1], locs[2], locs[3], locs[4]);
	}

	public static BlockState GetBlockState(CompoundNBT tag, String key)
	{
		return NBTUtil.readBlockState(tag.getCompound(key));
	}

	public static void SetBlockState(CompoundNBT tag, BlockState state, String key)
	{
		tag.put(key, NBTUtil.writeBlockState(state));
	}

	public static ItemStack SetTag(ItemStack stack, String key, CompoundNBT tag)
	{
		stack.getOrCreateSubCompound(key);
		stack.getTagCompound().putTag(key, tag);
		return stack;
	}

	public static Gearbox.GearType GetGear(CompoundNBT compound, String key)
	{
		int integer = compound.getInteger(key);
		if (integer == -1)
			return null;
		return Gearbox.GearType.values()[integer];
	}

	public static void SetGear(CompoundNBT compound, String key, Gearbox.GearType gear)
	{
		if (gear == null)
			compound.putInteger(key, -1);
		else
			compound.putInteger(key, gear.GetId());
	}

	public static <K, V> void SetMap(CompoundNBT compound, String key, Map<K, V> map,
			Function<K, String> keySerializer, Function<V, NBTBase> valueSerializer)
	{
		CompoundNBT tag = new CompoundNBT();
		for (Map.Entry<K, V> entry : map.entrySet())
		{
			tag.putTag(keySerializer.apply(entry.getKey()), valueSerializer.apply(entry.getValue()));
		}
		compound.putTag(key, tag);
	}

	public static <K, V> Map<K, V> GetMap(CompoundNBT compound, String key, Function<String, K> keyDeserializer,
			Function<NBTBase, V> valueDeserializer)
	{
		Map<K, V> map = new HashMap<>();
		CompoundNBT tag = compound.getCompoundTag(key);
		for (String k : tag.getKeySet())
		{
			map.put(keyDeserializer.apply(k), valueDeserializer.apply(tag.getTag(k)));
		}
		return map;
	}
}
