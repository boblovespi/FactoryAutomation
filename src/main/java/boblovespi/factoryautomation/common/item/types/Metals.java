package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 11/9/2017.
 */
public enum Metals implements IStringSerializable, IMultiTypeEnum
{
	IRON(0, "iron"),
	GOLD(1, "gold"),
	COPPER(2, "copper"),
	TIN(3, "tin"),
	BRONZE(4, "bronze"),
	STEEL(5, "steel"),
	PIG_IRON(6, "pig_iron"),
	MAGMATIC_BRASS(7, "magmatic_brass"),
	SILVER(8, "silver"),
	LEAD(9, "lead");
	private final int id;
	private final String name;

	Metals(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public static Metals GetFromName(String name)
	{
		return Metals.valueOf(name.toUpperCase());
	}

	@Override
	public String getName()
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
