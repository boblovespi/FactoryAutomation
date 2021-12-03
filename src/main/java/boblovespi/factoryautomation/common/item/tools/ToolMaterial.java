package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import boblovespi.factoryautomation.common.util.FATags;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static boblovespi.factoryautomation.common.config.ConfigFields.toolMiningLevelCat;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToolMaterial implements Tier
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
			WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.of(Items.FLINT));
	public static int STONE = toolMiningLevelCat.stone;
	public static int COPPER = toolMiningLevelCat.copper;
	public static int IRON = toolMiningLevelCat.iron;
	public static int BRONZE = toolMiningLevelCat.bronze;
	public static ToolMaterial bronzeMaterial = new ToolMaterial(
			BRONZE, 600, 5.0F, 2.0F, 12, () -> Ingredient.of(FATags.ForgeItemTag("ingots/bronze")));
	public static int DIAMOND = toolMiningLevelCat.diamond;
	public static int STEEL = toolMiningLevelCat.steel;
	public static int NETHERITE = toolMiningLevelCat.netherite;
	public static ToolMaterial steelMaterial = new ToolMaterial(
			STEEL, 1920, 6.5f, 8, 3, () -> Ingredient.of(FATags.ForgeItemTag("ingots/steel")));
	public static ToolMaterial copperMaterial = new ToolMaterial(COPPER, 240, 3.5f, 1.5f, 5,
			() -> Ingredient.of(FATags.ForgeItemTag("ingots/copper")));
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
		flintMaterial = new ToolMaterial(WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.of(Items.FLINT));
		STONE = toolMiningLevelCat.stone;
		COPPER = toolMiningLevelCat.copper;
		IRON = toolMiningLevelCat.iron;
		BRONZE = toolMiningLevelCat.bronze;
		bronzeMaterial = new ToolMaterial(
				BRONZE, 351, 5.0F, 2.0F, 12, () -> Ingredient.of(FATags.ForgeItemTag("ingots/bronze")));
		DIAMOND = toolMiningLevelCat.diamond;
		STEEL = toolMiningLevelCat.steel;
		steelMaterial = new ToolMaterial(
				STEEL, 1920, 6.5f, 8, 3, () -> Ingredient.of(FATags.ForgeItemTag("ingots/steel")));
	}

	@Override
	public int getUses()
	{
		return maxUses;
	}

	@Override
	public float getSpeed()
	{
		return efficiency;
	}

	@Override
	public float getAttackDamageBonus()
	{
		return attackDamage;
	}

	@Override
	public int getLevel()
	{
		return harvestLevel;
	}

	@Override
	public int getEnchantmentValue()
	{
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairMaterial.get();
	}
}
