package boblovespi.factoryautomation.common.block.resource;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Willi on 5/24/2018.
 * a class that holds information about an ore
 */
public class OreData
{
	final Item ore;
	public int miningLevel = 0;
	public float hardness = 0;
	public float resistance = 0;
	Function<Random, Integer> dropChance;
	BiFunction<RandomSource, Integer, Integer> xpChance;

	public OreData(Item ore)
	{
		this.ore = ore;
	}

	public OreData SetDropChance(Function<Random, Integer> func)
	{
		dropChance = func;
		return this;
	}

	public OreData SetXpChance(BiFunction<RandomSource, Integer, Integer> func)
	{
		xpChance = func;
		return this;
	}

	public OreData SetMiningLevel(int miningLevel)
	{
		this.miningLevel = miningLevel;
		return this;
	}

	public OreData SetHardness(float hardness)
	{
		this.hardness = hardness;
		return this;
	}

	public OreData SetResistance(float resistance)
	{
		this.resistance = resistance;
		return this;
	}
}
