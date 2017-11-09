package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 11/9/2017.
 */
public enum Metals implements IStringSerializable, IMultiTypeEnum
{
	COPPER(0, "copper"),
	TIN(1, "tin"),
	IRON(2, "iron"),
	GOLD(3, "gold"),
	BRONZE(4, "bronze"),
	STEEL(5, "steel");
	private int id;
	private String name;

	private Metals(int id, String name)
	{
		this.id = id;
		this.name = name;
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
