package boblovespi.factoryautomation.common.item.types;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 7/1/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum TallowForms implements IMultiTypeEnum, IStringSerializable
{
	CUBE("cube"), INGOT("ingot"), SHEET("sheet"), COIN("coin"), NUGGET("nugget"), ROD("rod"), GEAR("gear");

	private final String name;

	TallowForms(String name)
	{
		this.name = name;
	}

	@Override
	public int getId()
	{
		return ordinal();
	}

	@Override
	public String getSerializedName()
	{
		return name;
	}
}
