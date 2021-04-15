package boblovespi.factoryautomation.common.item.types;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;

/**
 * Created by Willi on 11/29/2019.
 */
public enum WoodTypes
{
	OAK(0, "oak", MaterialColor.WOOD),
	SPRUCE(1, "spruce", MaterialColor.PODZOL),
	BIRCH(2, "birch", MaterialColor.SAND),
	JUNGLE(3, "jungle", MaterialColor.DIRT),
	ACACIA(4, "acacia", MaterialColor.COLOR_ORANGE),
	DARK_OAK(5, "dark_oak", MaterialColor.COLOR_BROWN);

	private final int i;
	private final String name;
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

	public Block GetLog()
	{
		switch (this)
		{
		case OAK:
			return Blocks.OAK_LOG;
		case SPRUCE:
			return Blocks.SPRUCE_LOG;
		case BIRCH:
			return Blocks.BIRCH_LOG;
		case JUNGLE:
			return Blocks.JUNGLE_LOG;
		case ACACIA:
			return Blocks.ACACIA_LOG;
		case DARK_OAK:
			return Blocks.DARK_OAK_LOG;
		}
		return Blocks.OAK_LOG;
	}

	public Item GetPlanks()
	{
		switch (this)
		{
		case OAK:
			return Items.OAK_PLANKS;
		case SPRUCE:
			return Items.SPRUCE_PLANKS;
		case BIRCH:
			return Items.BIRCH_PLANKS;
		case JUNGLE:
			return Items.JUNGLE_PLANKS;
		case ACACIA:
			return Items.ACACIA_PLANKS;
		case DARK_OAK:
			return Items.DARK_OAK_PLANKS;
		}
		return Items.OAK_PLANKS;
	}
}
