package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.config.SyncOnConfigChange;
import boblovespi.factoryautomation.common.config.SyncOnConfigChange.Priority;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static boblovespi.factoryautomation.FactoryAutomation.MODID;
import static boblovespi.factoryautomation.common.config.ConfigFields.blockMiningLevelCat;
import static boblovespi.factoryautomation.common.item.tools.ToolMaterial.*;

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
		ItemStack stack = event.getPlayer().getHeldItemMainhand();
		BlockState state = event.getTargetBlock();
		ToolType tool = state.getHarvestTool();
		int toolLevel = stack.getItem().getHarvestLevel(stack, tool, event.getPlayer(), state);
		if (state.getBlock() == Blocks.IRON_ORE)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.ironOre);
		if (state.getBlock() == Blocks.DIAMOND_ORE)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.diamondOre);
		if (state.getBlock() == Blocks.REDSTONE_ORE)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.redstoneOre);
		if (state.getBlock() == Blocks.LAPIS_ORE)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.lapisOre);
		if (state.getBlock() == Blocks.GOLD_ORE)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.goldOre);
		if (state.getBlock() == Blocks.OBSIDIAN)
			event.setCanHarvest(toolLevel >= blockMiningLevelCat.obsidian);
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
		try
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
		}
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
