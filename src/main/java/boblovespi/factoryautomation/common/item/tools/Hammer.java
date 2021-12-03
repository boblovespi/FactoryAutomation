package boblovespi.factoryautomation.common.item.tools;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Tier;

import java.util.Collections;

import net.minecraft.world.item.Item.Properties;

/**
 * Created by Willi on 5/13/2018.
 */
public class Hammer extends WorkbenchToolItem
{
	public Hammer(String name, float attackDamageIn, float attackSpeedIn, Tier materialIn)
	{
		super(name, attackDamageIn, attackSpeedIn, materialIn, Collections.singleton(Blocks.STONE), new Properties(),
				FAToolTypes.HAMMER);
	}
}
