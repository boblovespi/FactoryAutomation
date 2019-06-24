package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

	public static void SetLocationTag(ItemStack stack, String key, int dim, int x, int y, int z)
	{
		GetTag(stack).setIntArray(key, new int[] { 31415, dim, x, y, z });
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
		GetTag(stack).setBoolean(key, val);
	}

	public static void ToggleBoolTag(ItemStack stack, String key)
	{
		SetBoolTag(stack, key, !GetBoolTag(stack, key));
	}

	public static void SetLocationTag(NBTTagCompound stack, String key, int dim, int x, int y, int z)
	{
		stack.setIntArray(key, new int[] { 31415, dim, x, y, z });
	}

	@Nullable
	public static DimLocation GetLocationTag(NBTTagCompound stack, String key)
	{
		int[] locs = stack.getIntArray(key);
		if (locs[0] != DimLocation.CLASS_KEY && locs.length != 5)
			return null;

		return new DimLocation(locs[1], locs[2], locs[3], locs[4]);
	}

	public static IBlockState GetBlockState(NBTTagCompound tag, String key)
	{
		NBTTagCompound base = tag.getCompoundTag(key);
		Block b = ForgeRegistries.BLOCKS
				.getValue(new ResourceLocation(base.getString("domain"), base.getString("path")));

		return b.getStateFromMeta(base.getInteger("meta"));
	}

	public static void SetBlockState(NBTTagCompound tag, IBlockState state, String key)
	{
		NBTTagCompound base = new NBTTagCompound();

		ResourceLocation block = state.getBlock().getRegistryName();

		base.setString("domain", block.getResourceDomain());
		base.setString("path", block.getResourcePath());
		base.setInteger("meta", state.getBlock().getMetaFromState(state));

		tag.setTag(key, base);
	}

	public static ItemStack SetTag(ItemStack stack, String key, NBTTagCompound tag)
	{
		stack.getOrCreateSubCompound(key);
		stack.getTagCompound().setTag(key, tag);
		return stack;
	}

	public static Gearbox.GearType GetGear(NBTTagCompound compound, String key)
	{
		int integer = compound.getInteger(key);
		if (integer == -1)
			return null;
		return Gearbox.GearType.values()[integer];
	}

	public static void SetGear(NBTTagCompound compound, String key, Gearbox.GearType gear)
	{
		if (gear == null)
			compound.setInteger(key, -1);
		else
			compound.setInteger(key, gear.GetId());
	}

	public static <K, V> void SetMap(NBTTagCompound compound, String key, Map<K, V> map,
			Function<K, String> keySerializer, Function<V, NBTBase> valueSerializer)
	{
		NBTTagCompound tag = new NBTTagCompound();
		for (Map.Entry<K, V> entry : map.entrySet())
		{
			tag.setTag(keySerializer.apply(entry.getKey()), valueSerializer.apply(entry.getValue()));
		}
		compound.setTag(key, tag);
	}

	public static <K, V> Map<K, V> GetMap(NBTTagCompound compound, String key, Function<String, K> keyDeserializer,
			Function<NBTBase, V> valueDeserializer)
	{
		Map<K, V> map = new HashMap<>();
		NBTTagCompound tag = compound.getCompoundTag(key);
		for (String k : tag.getKeySet())
		{
			map.put(keyDeserializer.apply(k), valueDeserializer.apply(tag.getTag(k)));
		}
		return map;
	}
}
