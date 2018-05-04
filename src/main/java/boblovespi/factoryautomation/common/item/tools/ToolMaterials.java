package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import static boblovespi.factoryautomation.common.config.ConfigFields.miningLevelCat;

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

	// TODO: populate from config

	public static int WOOD = miningLevelCat.wood;
	public static int STONE = miningLevelCat.stone;
	public static int COPPER = miningLevelCat.copper;
	public static int IRON = miningLevelCat.iron;
	public static int BRONZE = miningLevelCat.bronze;
	public static ToolMaterial bronzeMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":bronze", BRONZE, 251, 5.0F, 2.0F, 12);
	public static int DIAMOND = miningLevelCat.diamond;
	public static int STEEL = miningLevelCat.steel;
	public static ToolMaterial steelMaterial = EnumHelper
			.addToolMaterial(FactoryAutomation.MODID + ":steel", STEEL, 1920, 6.5f, 8, 3);

	@SyncOnConfigChange(priority = SyncOnConfigChange.Priority.INIT_FIELDS)
	public static void RefreshMiningLevels()
	{
		WOOD = miningLevelCat.wood;
		STONE = miningLevelCat.stone;
		COPPER = miningLevelCat.copper;
		IRON = miningLevelCat.iron;
		BRONZE = miningLevelCat.bronze;
		bronzeMaterial = EnumHelper.addToolMaterial(FactoryAutomation.MODID + ":bronze", BRONZE, 251, 5.0F, 2.0F, 12);
		DIAMOND = miningLevelCat.diamond;
		STEEL = miningLevelCat.steel;
		steelMaterial = EnumHelper.addToolMaterial(FactoryAutomation.MODID + ":steel", STEEL, 1920, 6.5f, 8, 3);
	}
}
