package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import static boblovespi.factoryautomation.common.config.ConfigFields.toolMiningLevelCat;

public class ToolMaterials
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

	public static int WOOD = toolMiningLevelCat.wood;
	public static int STONE = toolMiningLevelCat.stone;
	public static int COPPER = toolMiningLevelCat.copper;
	public static int IRON = toolMiningLevelCat.iron;
	public static int BRONZE = toolMiningLevelCat.bronze;
	public static ToolMaterial bronzeMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":bronze", BRONZE, 251, 5.0F, 2.0F, 12);
	public static int DIAMOND = toolMiningLevelCat.diamond;
	public static int STEEL = toolMiningLevelCat.steel;
	public static ToolMaterial steelMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":steel", STEEL, 1920, 6.5f, 8, 3);

	@SyncOnConfigChange(priority = SyncOnConfigChange.Priority.INIT_FIELDS)
	public static void RefreshMiningLevels()
	{
		WOOD = toolMiningLevelCat.wood;
		STONE = toolMiningLevelCat.stone;
		COPPER = toolMiningLevelCat.copper;
		IRON = toolMiningLevelCat.iron;
		BRONZE = toolMiningLevelCat.bronze;
		bronzeMaterial = EnumHelper.addToolMaterial(FactoryAutomation.MODID + ":bronze", BRONZE, 251, 5.0F, 2.0F, 12);
		DIAMOND = toolMiningLevelCat.diamond;
		STEEL = toolMiningLevelCat.steel;
		steelMaterial = EnumHelper.addToolMaterial(FactoryAutomation.MODID + ":steel", STEEL, 1920, 6.5f, 8, 3);
	}
}
