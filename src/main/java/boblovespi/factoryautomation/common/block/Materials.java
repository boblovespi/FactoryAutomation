package boblovespi.factoryautomation.common.block;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * Created by Willi on 6/27/2018.
 */
public class Materials
{
	public static final Material SAP = new Material.Builder(MaterialColor.COLOR_LIGHT_GRAY).noCollider().nonSolid().replaceable().liquid().build();
	public static final Material ROCKS = new Material.Builder(MaterialColor.STONE).replaceable().build();
	public static final Material WOOD_MACHINE = new Material.Builder(MaterialColor.WOOD).build();
	public static final Material BLOOM = new Material.Builder(MaterialColor.METAL).build();  // Note: requiresTool was moved to the uses with AbstractBlock.Properties.
}
