package boblovespi.factoryautomation.common.item.tools;

import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;

import java.util.Collections;

/**
 * Created by Willi on 5/13/2018.
 */
public class Hammer extends WorkbenchToolItem
{
	public Hammer(String name, float attackDamageIn, float attackSpeedIn, IItemTier materialIn)
	{
		super(
				name, attackDamageIn, attackSpeedIn, materialIn, Collections.singleton(Blocks.STONE), new Properties(),
				FAToolTypes.HAMMER);
	}
}
