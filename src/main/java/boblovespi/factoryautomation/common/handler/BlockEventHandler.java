package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import boblovespi.factoryautomation.common.tileentity.processing.TEChoppingBlock;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Willi on 4/10/2018.
 */

@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID)
public class BlockEventHandler
{
	private static final Map<BlockPos, Set<Direction>> ashFires = new HashMap<>();
	//	private static final Set<Block> flamableBlocks = new HashSet<Block>()
	//	{{
	//		add(Blocks.PLANKS);
	//		add(Blocks.LOG);
	//		add(Blocks.LOG2);
	//		// TODO: add the rest of the blocks (also migrate to tags)
	//	}};

	/*
	@SubscribeEvent
	public static void OnBlockHarvestedEvent(BlockEvent.HarvestDropsEvent event)
	{
		if (Blocks.DIAMOND_ORE == event.getState().getBlock())
		{
			if (event.getFortuneLevel() > 0)
				return;

			event.setDropChance(1f);
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(Blocks.DIAMOND_ORE));

		} else if (Blocks.STONE == event.getState().getBlock())
		{
			if (event.isSilkTouching())
				return;

			if (event.getHarvester() == null || !(event.getHarvester().getHeldItem(event.getHarvester().getActiveHand())
													   .getItem() instanceof Hammer))
				return;

			int fortune = event.getFortuneLevel();
			Random random = event.getHarvester().getRNG();
			int i = random.nextInt(fortune + 2) - 1;

			if (i < 0)
			{
				i = 0;
			}

			event.setDropChance(1f);
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(FAItems.stoneDust.ToItem(), 2 * (i + 1)));
		} else if (FABlocks.rock == event.getState().getBlock())
		{
			if (event.isSilkTouching())
				return;

			if (event.getHarvester() == null || !(event.getHarvester().getHeldItem(event.getHarvester().getActiveHand())
													   .getItem() instanceof Hammer))
				return;

			event.setDropChance(1f);
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(FAItems.stoneDust.ToItem(), 1));
		} else if (Blocks.GRASS == event.getState().getBlock())
		{
			if (event.isSilkTouching())
				return;

			if (event.getHarvester() == null || !(
					event.getHarvester().getHeldItem(event.getHarvester().getActiveHand()).getItem()
							== FAItems.choppingBlade))
				return;

			event.setDropChance(1f);
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(Blocks.GRASS, 1));
			ItemHelper.DamageItem(event.getHarvester().getHeldItem(event.getHarvester().getActiveHand()), 1);
		}
	}
	 */

	@SubscribeEvent
	public static void OnBlockBrokenEvent(BlockEvent.BreakEvent event)
	{
		BlockPos pos = event.getPos();
		LevelAccessor world = event.getLevel();
		if (FABlocks.multiblockPart == event.getState().getBlock())
		{
			Log.LogInfo("something broke!");
			Log.LogInfo("blockPos", pos);

			TEMultiblockPart part = (TEMultiblockPart) world.getBlockEntity(pos);

			Log.LogInfo("part is null", part == null);

			if (part != null)
			{
				MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
				int[] offset = part.GetOffset();

				BlockPos controllerLoc = pos.offset(-offset[0], -offset[1], -offset[2]);
				BlockEntity te = world.getBlockEntity(controllerLoc);
				if (te instanceof IMultiblockControllerTE)
				{
					IMultiblockControllerTE controllerTe = (IMultiblockControllerTE) te;
					controllerTe.BreakStructure();
					controllerTe.SetStructureInvalid();
				}
				if (part.GetBlockState() != null)
					Block.dropResources(part.GetBlockState(), part.getLevel(), pos);
			}
		} else if (world.getBlockEntity(pos) instanceof IMultiblockControllerTE)
		{
			Log.LogInfo("something broke!");
			Log.LogInfo("blockPos", pos);

			IMultiblockControllerTE part = (IMultiblockControllerTE) world.getBlockEntity(pos);

			part.BreakStructure();
			part.SetStructureInvalid();

		} else if (Blocks.DIAMOND_ORE == event.getState().getBlock())
		{
			int enchantmentLevel = EnchantmentHelper
					.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, event.getPlayer());
			if (enchantmentLevel == 0)
				event.setExpToDrop(0);

		}
	}

	@SubscribeEvent
	public static void OnNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event)
	{
		BlockState state = event.getState();
		BlockPos pos = event.getPos().immutable();
		LevelAccessor level = event.getLevel();
		if (state.getBlock() == Blocks.FIRE)
		{
			if (!ashFires.containsKey(pos))
			{
				ashFires.put(pos, new HashSet<>(6));
				for (Direction offset : Direction.values())
				{
					if (FATags.Contains(FATags.FABlockTag("gives_ash"), level.getBlockState(pos.relative(offset)).getBlock()))
						ashFires.get(pos).add(offset);
					else
						ashFires.get(pos).remove(offset);
				}
			}
		} else if (state.getBlock() == Blocks.AIR)
		{
			if (ashFires.containsKey(pos))
			{
				for (Direction facing : ashFires.get(pos))
				{
					BlockPos oldWoodPos = pos.relative(facing);
					CheckAndSpawnAsh(level, level.getBlockState(oldWoodPos), oldWoodPos);
				}
				ashFires.remove(pos);
			}
		}
	}

	@SubscribeEvent
	public static void OnLeftClickBLockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		BlockState state = event.getLevel().getBlockState(event.getPos());
		BlockPos pos = event.getPos().immutable();
		Level world = event.getLevel();
		Player player = event.getEntity();

		if (FATags.Contains(BlockTags.LOGS, state.getBlock()))
		{
			if (!FATags.Contains(FATags.FAItemTag("tools/axes"), player.getItemInHand(InteractionHand.MAIN_HAND).getItem()))
			{
				player.hurt(DamageSource.GENERIC, 1);
				event.setUseBlock(Event.Result.DENY);
				event.setUseItem(Event.Result.DENY);
			}
		}
		if (FABlocks.woodChoppingBlocks.contains(state.getBlock()))
		{
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof TEChoppingBlock)
				((TEChoppingBlock) te).LeftClick(player.getItemInHand(InteractionHand.MAIN_HAND));
		}
	}

	private static void CheckAndSpawnAsh(LevelAccessor level, BlockState state, BlockPos pos)
	{
		if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.AIR)
		{
			ItemEntity item = new ItemEntity((Level) level, pos.getX(), pos.getY(), pos.getZ(),
					new ItemStack(FAItems.ash, level.getRandom().nextInt(2) + 1));
			item.setInvulnerable(true);
			level.addFreshEntity(item);
		}
	}
}