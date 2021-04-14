package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 7/1/2019.
 */
public enum TallowForms implements IMultiTypeEnum, IStringSerializable
{
	CUBE("cube"), INGOT("ingot"), SHEET("sheet"), COIN("coin"), NUGGET("nugget"), ROD("rod"), GEAR("gear");

	private final String name;

	TallowForms(String name)
	{
		this.name = name;
	}

	@Override
	public int GetId()
	{
		return ordinal();
	}

	@Override
	public String getString()
	{
		return name;
	}
}
