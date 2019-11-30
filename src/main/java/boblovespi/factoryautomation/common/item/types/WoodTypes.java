package boblovespi.factoryautomation.common.item.types;

import net.minecraft.block.material.MaterialColor;

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
}
