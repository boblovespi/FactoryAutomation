package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import boblovespi.factoryautomation.common.config.SyncOnConfigChange.Priority;
import boblovespi.factoryautomation.common.item.tools.ToolMaterial;
import boblovespi.factoryautomation.common.util.FATags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.config.ConfigFields.blockMiningLevelCat;

/**
 * Created by Willi on 4/30/2018.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class VanillaTweakHandler
{
	private static final String pick = "pickaxe";

	/*
		Harvest levels:
		0: wood
		1: stone
		2: copper
		3: iron
		4: bronze
		5: diamond / steel

		 */

	@SubscribeEvent
	public static void CheckMiningLevel(PlayerEvent.HarvestCheck event)
	{
		ItemStack stack = event.getPlayer().getMainHandItem();
		BlockState state = event.getTargetBlock();
		Tier tier = Tiers.WOOD;
		if (stack.getItem() instanceof TieredItem te)
			tier = te.getTier();
		if (state.is(BlockTags.IRON_ORES))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != Tiers.IRON).anyMatch(tier::equals));
		if (state.is(BlockTags.DIAMOND_ORES))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != ToolMaterial.bronzeMaterial).anyMatch(tier::equals));
		if (state.is(BlockTags.REDSTONE_ORES))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != ToolMaterial.bronzeMaterial).anyMatch(tier::equals));
		if (state.is(BlockTags.LAPIS_ORES))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != ToolMaterial.copperMaterial).anyMatch(tier::equals));
		if (state.is(BlockTags.GOLD_ORES))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != ToolMaterial.bronzeMaterial).anyMatch(tier::equals));
		if (state.is(Tags.Blocks.OBSIDIAN))
			event.setCanHarvest(TierSortingRegistry.getSortedTiers().stream().dropWhile(t -> t != ToolMaterial.steelMaterial).anyMatch(tier::equals)); // TODO: figure out how to do better probably by modifying tags
	}

	@SyncOnConfigChange(priority = Priority.FIRST)
	public static void TweakMiningLevels()
	{
		// temp
		//		try
		//		{
		//			Field harvestLevel = Block.class.getDeclaredField("harvestLevel");
		//			if (Modifier.isPrivate(harvestLevel.getModifiers()))
		//			{
		//				harvestLevel.setAccessible(true);
		//				harvestLevel.setInt(Blocks.IRON_ORE, blockMiningLevelCat.ironOre);
		//				harvestLevel.setInt(Blocks.DIAMOND_ORE, blockMiningLevelCat.diamondOre);
		//				harvestLevel.setInt(Blocks.REDSTONE_ORE, blockMiningLevelCat.redstoneOre);
		//				harvestLevel.setInt(Blocks.LAPIS_ORE, blockMiningLevelCat.lapisOre);
		//				harvestLevel.setInt(Blocks.GOLD_ORE, blockMiningLevelCat.goldOre);
		//				harvestLevel.setInt(Blocks.OBSIDIAN, blockMiningLevelCat.obsidian);
		//			}
		//		} catch (NoSuchFieldException | IllegalAccessException e)
		//		{
		//			e.printStackTrace();
		//		}
	}

	@SyncOnConfigChange(priority = Priority.FIRST)
	public static void TweakToolLevels()
	{
		Tiers.WOOD.level = ToolMaterial.WOOD;
		Tiers.STONE.level = ToolMaterial.STONE;
		Tiers.GOLD.level = ToolMaterial.COPPER;
		Tiers.IRON.level = ToolMaterial.IRON;
		Tiers.DIAMOND.level = ToolMaterial.DIAMOND;
		Tiers.NETHERITE.level = ToolMaterial.NETHERITE;

/*		try
		{
			Field harvestLevel = ItemTier.class.getDeclaredFields()[0];
			if (Modifier.isPrivate(harvestLevel.getModifiers()) && Modifier.isFinal(harvestLevel.getModifiers()))
			{
				harvestLevel.setAccessible(true);
				harvestLevel.setInt(ItemTier.WOOD, WOOD);
				harvestLevel.setInt(ItemTier.STONE, STONE);
				harvestLevel.setInt(ItemTier.GOLD, COPPER);
				harvestLevel.setInt(ItemTier.IRON, IRON);
				harvestLevel.setInt(ItemTier.DIAMOND, DIAMOND);
				// harvestLevel.setInt(ItemTier.DIAMOND, NETHERITE); add in 1.16!
			}
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}*/

		Items.IRON_AXE.maxDamage = 320;
		Items.IRON_PICKAXE.maxDamage = 320;
		Items.IRON_HOE.maxDamage = 320;
		Items.IRON_SHOVEL.maxDamage = 320;
		Items.IRON_SWORD.maxDamage = 320;
	}

	public static void RemoveItems(RegistryEvent.Register<Item> event)
	{
		if (event.getRegistry() instanceof ForgeRegistry)
		{
			ForgeRegistry<Item> registry = (ForgeRegistry<Item>) event.getRegistry();
			registry.remove(new ResourceLocation("minecraft", "wooden_pickaxe"));
		}
	}
}
