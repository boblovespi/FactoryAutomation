package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

import static boblovespi.factoryautomation.common.config.ConfigFields.toolMiningLevelCat;

public class ToolMaterial implements IItemTier
{
	/*
		Harvest levels:
		0: wood
		1: stone
		2: copper
		3: iron
		4: bronze
		5: diamond / steel
		6: netherite / new alloy
		 */

	public static int WOOD = toolMiningLevelCat.wood;
	public static ToolMaterial flintMaterial = new ToolMaterial(
			WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.fromItems(Items.FLINT));
	public static int STONE = toolMiningLevelCat.stone;
	public static int COPPER = toolMiningLevelCat.copper;
	public static int IRON = toolMiningLevelCat.iron;
	public static int BRONZE = toolMiningLevelCat.bronze;
	public static ToolMaterial bronzeMaterial = new ToolMaterial(
			BRONZE, 351, 5.0F, 2.0F, 12, () -> Ingredient.fromTag(FATags.ForgeItemTag("ingots/bronze")));
	public static int DIAMOND = toolMiningLevelCat.diamond;
	public static int STEEL = toolMiningLevelCat.steel;
	public static int NETHERITE = toolMiningLevelCat.netherite;
	public static ToolMaterial steelMaterial = new ToolMaterial(
			STEEL, 1920, 6.5f, 8, 3, () -> Ingredient.fromTag(FATags.ForgeItemTag("ingots/steel")));
	public static ToolMaterial copperMaterial = new ToolMaterial(COPPER, 180, 3.5f, 1.5f, 5,
			() -> Ingredient.fromTag(FATags.ForgeItemTag("ingots/copper")));
	//

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairMaterial;

	private ToolMaterial(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
			int enchantabilityIn, Supplier<Ingredient> repairMaterialIn)
	{
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = repairMaterialIn;
	}

	@SyncOnConfigChange(priority = SyncOnConfigChange.Priority.INIT_FIELDS)
	public static void RefreshMiningLevels()
	{
		WOOD = toolMiningLevelCat.wood;
		flintMaterial = new ToolMaterial(WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.fromItems(Items.FLINT));
		STONE = toolMiningLevelCat.stone;
		COPPER = toolMiningLevelCat.copper;
		IRON = toolMiningLevelCat.iron;
		BRONZE = toolMiningLevelCat.bronze;
		bronzeMaterial = new ToolMaterial(
				BRONZE, 351, 5.0F, 2.0F, 12, () -> Ingredient.fromTag(FATags.ForgeItemTag("ingots/bronze")));
		DIAMOND = toolMiningLevelCat.diamond;
		STEEL = toolMiningLevelCat.steel;
		steelMaterial = new ToolMaterial(
				STEEL, 1920, 6.5f, 8, 3, () -> Ingredient.fromTag(FATags.ForgeItemTag("ingots/steel")));
	}

	@Override
	public int getMaxUses()
	{
		return maxUses;
	}

	@Override
	public float getEfficiency()
	{
		return efficiency;
	}

	@Override
	public float getAttackDamage()
	{
		return attackDamage;
	}

	@Override
	public int getHarvestLevel()
	{
		return harvestLevel;
	}

	@Override
	public int getEnchantability()
	{
		return enchantability;
	}

	@Override
	public Ingredient getRepairMaterial()
	{
		return repairMaterial.get();
	}
}
