package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 11/9/2017.
 */
public enum Metals implements IStringSerializable, IMultiTypeEnum
{
	IRON(0, "iron", 1538, 0xFFEAEEF2),
	GOLD(1, "gold", 10000, 0xFFFAF437),
	COPPER(2, "copper", 1084, 0xFFFF973D),
	TIN(3, "tin", 232, 0xFFF7E8E8),
	BRONZE(4, "bronze", 950, 0xFFFFB201),
	STEEL(5, "steel", 10000, 0xFF000000),
	PIG_IRON(6, "pig_iron", 10000, 0xFF000000),
	MAGMATIC_BRASS(7, "magmatic_brass", 10000, 0xFF000000),
	SILVER(8, "silver", 10000, 0xFF000000),
	LEAD(9, "lead", 10000, 0xFF000000);
	private final int id;
	private final String name;
	public final int meltTemp;
	public final int color;

	Metals(int id, String name, int meltTemp, int color)
	{
		this.id = id;
		this.name = name;
		this.meltTemp = meltTemp;
		this.color = color;
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
