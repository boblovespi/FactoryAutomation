package boblovespi.factoryautomation.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

/**
 * Created by Willi on 6/27/2018.
 */
public class Materials
{
	public static final Material SAP = new MaterialLiquid(MapColor.SILVER);
	public static final Material ROCKS = new Material(MapColor.STONE).setReplaceable();
	public static final Material WOOD_MACHINE = new Material(MapColor.WOOD);
}
