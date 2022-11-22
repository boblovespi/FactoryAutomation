package boblovespi.factoryautomation.common.item.types;

import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;

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
		if (FATags.Contains(BlockTags.OAK_LOGS, log))
			return OAK;
		if (FATags.Contains(BlockTags.SPRUCE_LOGS, log))
			return SPRUCE;
		if (FATags.Contains(BlockTags.BIRCH_LOGS, log))
			return BIRCH;
		if (FATags.Contains(BlockTags.JUNGLE_LOGS, log))
			return JUNGLE;
		if (FATags.Contains(BlockTags.ACACIA_LOGS, log))
			return ACACIA;
		if (FATags.Contains(BlockTags.DARK_OAK_LOGS, log))
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
