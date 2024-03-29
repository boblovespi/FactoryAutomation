package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Willi on 11/26/2017.
 */
public class NBTHelper
{
	public static MultiblockStructurePattern GetStructurePattern(CompoundTag compound, String key)
	{
		return MultiblockHandler.Get(compound.getString(key));
	}

	public static CompoundTag GetTag(ItemStack items)
	{
		if (!items.hasTag())
		{
			items.setTag(new CompoundTag());
		}
		return items.getTag();
	}

	public static boolean HasKey(ItemStack stack, String key)
	{
		return HasTag(stack) && GetTag(stack).contains(key);
	}

	public static boolean HasTag(ItemStack stack)
	{
		return stack.hasTag();
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

	public static void SetLocationTag(CompoundTag stack, String key, int dim, int x, int y, int z)
	{
		stack.putIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(CompoundTag stack, String key)
	{
		int[] locs = stack.getIntArray(key);
		if (locs[0] != DimLocation.CLASS_KEY && locs.length != 5)
			return null;

		return new DimLocation(locs[1], locs[2], locs[3], locs[4]);
	}

	public static BlockState GetBlockState(CompoundTag tag, String key)
	{
		return NbtUtils.readBlockState(tag.getCompound(key));
	}

	public static void SetBlockState(CompoundTag tag, BlockState state, String key)
	{
		tag.put(key, NbtUtils.writeBlockState(state));
	}

	public static ItemStack SetTag(ItemStack stack, String key, CompoundTag tag)
	{
		stack.getOrCreateTag().put(key, tag);
		return stack;
	}

	public static Gearbox.GearType GetGear(CompoundTag compound, String key)
	{
		int integer = compound.getInt(key);
		if (integer == -1)
			return null;
		return Gearbox.GearType.values()[integer];
	}

	public static void SetGear(CompoundTag compound, String key, Gearbox.GearType gear)
	{
		if (gear == null)
			compound.putInt(key, -1);
		else
			compound.putInt(key, gear.GetId());
	}

	public static <K, V> void SetMap(CompoundTag compound, String key, Map<K, V> map,
			Function<K, String> keySerializer, Function<V, Tag> valueSerializer)
	{
		CompoundTag tag = new CompoundTag();
		for (Map.Entry<K, V> entry : map.entrySet())
		{
			tag.put(keySerializer.apply(entry.getKey()), valueSerializer.apply(entry.getValue()));
		}
		compound.put(key, tag);
	}

	public static <K, V> Map<K, V> GetMap(CompoundTag compound, String key, Function<String, K> keyDeserializer,
			Function<Tag, V> valueDeserializer)
	{
		Map<K, V> map = new HashMap<>();
		CompoundTag tag = compound.getCompound(key);
		for (String k : tag.getAllKeys())
		{
			map.put(keyDeserializer.apply(k), valueDeserializer.apply(tag.get(k)));
		}
		return map;
	}
}
