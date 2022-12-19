package boblovespi.factoryautomation.common.item.tools;

import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.TierSortingRegistry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
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

	public static Tier badFlintMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.of(Items.FLINT)),
			new ResourceLocation(MODID, "flint"), List.of(Tiers.WOOD), List.of(Tiers.STONE));
	public static Tier goodFlintMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(WOOD, Tiers.WOOD.getUses(), Tiers.WOOD.getSpeed(), Tiers.WOOD.getAttackDamageBonus(),
					Tiers.WOOD.getEnchantmentValue(), () -> Ingredient.of(Items.FLINT)),
			new ResourceLocation(MODID, "good_flint"), List.of(badFlintMaterial), List.of(Tiers.STONE));

	public static int STONE = toolMiningLevelCat.stone;
	public static int COPPER = toolMiningLevelCat.copper;
	public static int IRON = toolMiningLevelCat.iron;
	public static int BRONZE = toolMiningLevelCat.bronze;

	public static Tier bronzeMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(BRONZE, 600, 5.0F, 2.0F, 12, () -> Ingredient.of(FATags.ForgeItemTag("ingots/bronze"))),
			new ResourceLocation(MODID, "bronze"), List.of(Tiers.IRON), List.of());

	public static int DIAMOND = toolMiningLevelCat.diamond;
	public static int STEEL = toolMiningLevelCat.steel;
	public static int NETHERITE = toolMiningLevelCat.netherite;

	public static Tier steelMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(STEEL, 1920, 6.5f, 4, 3, () -> Ingredient.of(FATags.ForgeItemTag("ingots/steel"))),
			new ResourceLocation(MODID, "steel"), List.of(bronzeMaterial), List.of(Tiers.DIAMOND));
	public static Tier copperMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(COPPER, 240, 3.5f, 1.5f, 5, () -> Ingredient.of(FATags.ForgeItemTag("ingots/copper"))),
			new ResourceLocation(MODID, "copper"), List.of(Tiers.STONE), List.of(Tiers.IRON));
	public static Tier voidsteelMaterial = TierSortingRegistry.registerTier(
			new ToolMaterial(NETHERITE, 2269, 20f, 6.5f, 25,
					() -> Ingredient.of(FATags.ForgeItemTag("ingots/voidsteel"))),
			new ResourceLocation(MODID, "voidsteel"), List.of(Tiers.NETHERITE), List.of());
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
		// flintMaterial = new ToolMaterial(WOOD, 20, 4.5f, 1.5f, 2, () -> Ingredient.of(Items.FLINT));
		STONE = toolMiningLevelCat.stone;
		COPPER = toolMiningLevelCat.copper;
		IRON = toolMiningLevelCat.iron;
		BRONZE = toolMiningLevelCat.bronze;
		// bronzeMaterial = new ToolMaterial(BRONZE, 351, 5.0F, 2.0F, 12, () -> Ingredient.of(FATags.ForgeItemTag("ingots/bronze")));
		DIAMOND = toolMiningLevelCat.diamond;
		STEEL = toolMiningLevelCat.steel;
		// steelMaterial = new ToolMaterial(STEEL, 1920, 6.5f, 8, 3, () -> Ingredient.of(FATags.ForgeItemTag("ingots/steel")));
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
