package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import boblovespi.factoryautomation.common.util.FATags;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

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

	@SubscribeEvent
	public static void OnBlockBrokenEvent(BlockEvent.BreakEvent event)
	{
		BlockPos pos = event.getPos();
		IWorld world = event.getWorld();
		if (FABlocks.multiblockPart == event.getState().getBlock())
		{
			Log.LogInfo("something broke!");
			Log.LogInfo("blockPos", pos);

			TEMultiblockPart part = (TEMultiblockPart) world.getTileEntity(pos);

			Log.LogInfo("part is null", part == null);

			if (part != null)
			{
				MultiblockStructurePattern structure = MultiblockHandler.Get(part.GetStructureId());
				int[] offset = part.GetOffset();

				BlockPos controllerLoc = pos.add(-offset[0], -offset[1], -offset[2]);
				TileEntity te = world.getTileEntity(controllerLoc);
				if (te instanceof IMultiblockControllerTE)
				{
					IMultiblockControllerTE controllerTe = (IMultiblockControllerTE) te;
					controllerTe.BreakStructure();
					controllerTe.SetStructureInvalid();
				}
			}
		} else if (world.getTileEntity(pos) instanceof IMultiblockControllerTE)
		{
			Log.LogInfo("something broke!");
			Log.LogInfo("blockPos", pos);

			IMultiblockControllerTE part = (IMultiblockControllerTE) world.getTileEntity(pos);

			part.BreakStructure();
			part.SetStructureInvalid();

		} else if (Blocks.DIAMOND_ORE == event.getState().getBlock())
		{
			int enchantmentLevel = EnchantmentHelper
					.getEnchantmentLevel(Enchantments.FORTUNE, event.getPlayer().getHeldItemMainhand());
			if (enchantmentLevel == 0)
				event.setExpToDrop(0);

		} else if (false)
		{

		}
	}

	@SubscribeEvent
	public static void OnNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent event)
	{
		BlockState state = event.getState();
		BlockPos pos = event.getPos().toImmutable();
		IWorld world = event.getWorld();
		if (state.getBlock() == Blocks.FIRE)
		{
			if (!ashFires.containsKey(pos))
			{
				ashFires.put(pos, new HashSet<>(6));
				for (Direction offset : Direction.values())
				{
					if (FATags.FABlockTag("gives_ash").contains(world.getBlockState(pos.offset(offset)).getBlock()))
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
					BlockPos oldWoodPos = pos.offset(facing);
					CheckAndSpawnAsh(world, world.getBlockState(oldWoodPos), oldWoodPos);
				}
				ashFires.remove(pos);
			}
		}
	}

	@SubscribeEvent
	public static void OnLeftClickBLockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		BlockState state = event.getWorld().getBlockState(event.getPos());
		BlockPos pos = event.getPos().toImmutable();
		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();

		if (BlockTags.LOGS.contains(state.getBlock()))
		{
			if (!FATags.FAItemTag("tools/axes").contains(player.getHeldItem(Hand.MAIN_HAND).getItem()))
			{
				player.attackEntityFrom(DamageSource.GENERIC, 1);
				event.setUseBlock(Event.Result.DENY);
				event.setUseItem(Event.Result.DENY);
			}
		}
	}

	private static void CheckAndSpawnAsh(IWorld world, BlockState state, BlockPos pos)
	{
		if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.AIR)
		{
			ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(),
					new ItemStack(FAItems.ash.ToItem(), world.getRandom().nextInt(2) + 1));
			item.setInvulnerable(true);
			world.addEntity(item);
		}
	}

}