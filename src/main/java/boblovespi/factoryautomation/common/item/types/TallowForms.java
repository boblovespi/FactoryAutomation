package boblovespi.factoryautomation.common.item.types;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 7/1/2019.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum TallowForms implements IMultiTypeEnum, StringRepresentable
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
	public String getSerializedName()
	{
		return name;
	}
}
