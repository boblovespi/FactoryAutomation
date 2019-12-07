package boblovespi.factoryautomation.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

/**
 * Created by Willi on 6/27/2018.
 */
public class Materials
{
	public static final Material SAP = new Material.Builder(MaterialColor.LIGHT_GRAY).doesNotBlockMovement().notSolid()
																					 .replaceable().liquid().build();
	public static final Material ROCKS = new Material.Builder(MaterialColor.STONE).replaceable().build();
	public static final Material WOOD_MACHINE = new Material.Builder(MaterialColor.WOOD).build();
}
