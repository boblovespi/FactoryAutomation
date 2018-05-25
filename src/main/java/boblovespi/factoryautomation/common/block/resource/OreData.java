package boblovespi.factoryautomation.common.block.resource;

import net.minecraft.item.Item;

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
	Function<Random, Integer> dropChance;
	BiFunction<Random, Integer, Integer> xpChance;

	public OreData(Item ore)
	{
		this.ore = ore;
	}

	public OreData SetDropChance(Function<Random, Integer> func)
	{
		dropChance = func;
		return this;
	}

	public OreData SetXpChance(BiFunction<Random, Integer, Integer> func)
	{
		xpChance = func;
		return this;
	}
}
