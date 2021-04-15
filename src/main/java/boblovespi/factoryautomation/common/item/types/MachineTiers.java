package boblovespi.factoryautomation.common.item.types;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 2/20/2018.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum MachineTiers implements IMultiTypeEnum, IStringSerializable
{
	IRON("iron", 0), OBSIDIAN("obsidian", 1);

	private final String name;
	private final int id;

	MachineTiers(String name, int id)
	{
		this.name = name;
		this.id = id;
	}

	@Override
	public int GetId()
	{
		return id;
	}

	@Override
	public String getSerializedName()
	{
		return name;
	}
}
