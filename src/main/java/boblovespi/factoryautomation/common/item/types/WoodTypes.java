package boblovespi.factoryautomation.common.item.types;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tags.BlockTags;

/**
 * Created by Willi on 11/29/2019.
 */
public enum WoodTypes
{
	OAK(0, "oak", MaterialColor.WOOD),
	SPRUCE(1, "spruce", MaterialColor.OBSIDIAN),
	BIRCH(2, "birch", MaterialColor.SAND),
	JUNGLE(3, "jungle", MaterialColor.DIRT),
	ACACIA(4, "acacia", MaterialColor.ADOBE),
	DARK_OAK(5, "dark_oak", MaterialColor.BROWN);

	private final int i;
	private String name;
	private final MaterialColor color;

	WoodTypes(int i, String name, MaterialColor color)
	{
		this.i = i;
		this.name = name;
		this.color = color;
	}

	public String GetName()
	{
		return name;
	}

	public MaterialColor GetColor()
	{
		return color;
	}

	public static WoodTypes FromLog(Block log)
	{
		if (BlockTags.OAK_LOGS.contains(log))
			return OAK;
		if (BlockTags.SPRUCE_LOGS.contains(log))
			return SPRUCE;
		if (BlockTags.BIRCH_LOGS.contains(log))
			return BIRCH;
		if (BlockTags.JUNGLE_LOGS.contains(log))
			return JUNGLE;
		if (BlockTags.ACACIA_LOGS.contains(log))
			return ACACIA;
		if (BlockTags.DARK_OAK_LOGS.contains(log))
			return DARK_OAK;
		return OAK;
	}

	public int Index()
	{
		return i;
	}
}
