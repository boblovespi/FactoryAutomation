package boblovespi.factoryautomation.common.item.types;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Willi on 2/20/2018.
 */
public enum MachineTiers implements IMultiTypeEnum, IStringSerializable
{
	IRON("iron", 0), OBSIDIAN("obsidian", 1);

	private String name;
	private int id;

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
	public String getString()
	{
		return name;
	}
}
