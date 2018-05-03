package boblovespi.factoryautomation.common.handler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import static boblovespi.factoryautomation.common.item.tools.ToolMaterials.*;

/**
 * Created by Willi on 4/30/2018.
 */
public class VanillaTweakHandler
{
	private static final String pick = "pickaxe";

	public static void TweakMiningLevels()
	{

		/*
		Harvest levels:
		0: wood
		1: stone
		2: copper
		3: iron
		4: bronze
		5: diamond / steel

		 */

		// temp
		Blocks.IRON_ORE.setHarvestLevel(pick, IRON);
		// temp

		Blocks.DIAMOND_ORE.setHarvestLevel(pick, BRONZE);
		Blocks.REDSTONE_ORE.setHarvestLevel(pick, IRON);
		Blocks.LAPIS_ORE.setHarvestLevel(pick, COPPER);
		Blocks.GOLD_ORE.setHarvestLevel(pick, BRONZE);
		Blocks.LIT_REDSTONE_ORE.setHarvestLevel(pick, IRON);
		Blocks.OBSIDIAN.setHarvestLevel(pick, DIAMOND);
	}

	public static void TweakToolLevels()
	{
		Items.IRON_PICKAXE.setHarvestLevel(pick, IRON);
		Items.DIAMOND_PICKAXE.setHarvestLevel(pick, DIAMOND);
		Items.GOLDEN_PICKAXE.setHarvestLevel(pick, COPPER);
		Items.STONE_PICKAXE.setHarvestLevel(pick, STONE);
		Items.WOODEN_PICKAXE.setHarvestLevel(pick, WOOD);

	}
}
