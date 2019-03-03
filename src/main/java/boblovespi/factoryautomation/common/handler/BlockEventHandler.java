package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TEMultiblockPart;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

/**
 * Created by Willi on 4/10/2018.
 */

@Mod.EventBusSubscriber
public class BlockEventHandler
{
	private static final Map<BlockPos, Set<EnumFacing>> ashFires = new HashMap<>();
	private static final Set<Block> flamableBlocks = new HashSet<Block>()
	{{
		add(Blocks.PLANKS);
		add(Blocks.LOG);
		add(Blocks.LOG2);
		// TODO: add the rest of the blocks
	}};

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
			if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE)
			{
				if (event.isSilkTouching())
					return;

				if (event.getHarvester() == null || !(event.getHarvester()
														   .getHeldItem(event.getHarvester().getActiveHand())
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
			}

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
		} else if (Blocks.TALLGRASS == event.getState().getBlock())
		{
			if (event.isSilkTouching())
				return;

			if (event.getHarvester() == null || !(
					event.getHarvester().getHeldItem(event.getHarvester().getActiveHand()).getItem()
							== FAItems.choppingBlade))
				return;

			event.setDropChance(1f);
			event.getDrops().clear();
			event.getDrops()
				 .add(new ItemStack(Blocks.TALLGRASS, 1, event.getState().getValue(BlockTallGrass.TYPE).getMeta()));
			ItemHelper.DamageItem(event.getHarvester().getHeldItem(event.getHarvester().getActiveHand()), 1);
		}
	}

	@SubscribeEvent
	public static void OnBlockBrokenEvent(BlockEvent.BreakEvent event)
	{
		BlockPos pos = event.getPos();
		World world = event.getWorld();
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
		IBlockState state = event.getState();
		BlockPos pos = event.getPos().toImmutable();
		World world = event.getWorld();
		if (state.getBlock() == Blocks.FIRE)
		{
			if (!ashFires.containsKey(pos))
			{
				ashFires.put(pos, new HashSet<>(6));
				for (EnumFacing offset : EnumFacing.values())
				{
					if (flamableBlocks.contains(world.getBlockState(pos.offset(offset)).getBlock()))
						ashFires.get(pos).add(offset);
					else
						ashFires.get(pos).remove(offset);
				}
			}
		} else if (state.getBlock() == Blocks.AIR)
		{
			if (ashFires.containsKey(pos))
			{
				for (EnumFacing facing : ashFires.get(pos))
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
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		BlockPos pos = event.getPos().toImmutable();
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();

		if (state.getBlock() instanceof BlockLog)
		{
			if (!(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemAxe))
			{
				player.attackEntityFrom(DamageSource.GENERIC, 1);
				event.setUseBlock(Event.Result.DENY);
				event.setUseItem(Event.Result.DENY);
			}
		}
	}

	private static void CheckAndSpawnAsh(World world, IBlockState state, BlockPos pos)
	{
		if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.AIR)
		{
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
					new ItemStack(FAItems.ash.ToItem(), world.rand.nextInt(2) + 1));
			item.setEntityInvulnerable(true);
			world.spawnEntity(item);
		}
	}

}