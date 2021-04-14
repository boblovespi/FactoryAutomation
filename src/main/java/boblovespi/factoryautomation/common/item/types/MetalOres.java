package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 12/23/2017.
 */
public enum MetalOres implements IMultiTypeEnum, IStringSerializable
{
	COPPER(0, "copper", 1),
	TIN(1, "tin", 1);

	public final int harvestLevel;
	private final int id;
	private final String name;

	MetalOres(int id, String name, int harvestLevel)
	{
		this.id = id;
		this.name = name;
		this.harvestLevel = harvestLevel;
	}

	@Override
	public String getString()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int GetId()
	{
		return id;
	}
}
