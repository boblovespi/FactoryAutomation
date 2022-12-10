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
	CUBE("cube", "storage_block"), INGOT("ingot"), SHEET("sheet", "plate"), COIN("coin"), NUGGET("nugget"), ROD("rod"), GEAR("gear");

	private final String name;
	private final String tagName;

	TallowForms(String name)
	{
		this.name = name;
		tagName = name;
	}

	TallowForms(String name, String tagName)
	{
		this.name = name;
		this.tagName = tagName;
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

	public String GetTagName()
	{
		return tagName;
	}
}
