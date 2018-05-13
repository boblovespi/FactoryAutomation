package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.tools.Hammer;
import boblovespi.factoryautomation.common.multiblock.IMultiblockControllerTE;
import boblovespi.factoryautomation.common.multiblock.MultiblockHandler;
import boblovespi.factoryautomation.common.multiblock.MultiblockStructurePattern;
import boblovespi.factoryautomation.common.tileentity.TileEntityMultiblockPart;
import boblovespi.factoryautomation.common.util.Log;
import net.minecraft.block.BlockStone;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

/**
 * Created by Willi on 4/10/2018.
 */

@Mod.EventBusSubscriber
public class BlockEventHandler
{
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

		} else if (false)
		{

		}
	}

	@SubscribeEvent
	public static void OnBlockBrokenEvent(BlockEvent.BreakEvent event)
	{
		if (FABlocks.multiblockPart == event.getState().getBlock())
		{
			BlockPos pos = event.getPos();
			World world = event.getWorld();

			Log.LogInfo("something broke!");
			Log.LogInfo("blockPos", pos);

			TileEntityMultiblockPart part = (TileEntityMultiblockPart) world.getTileEntity(pos);

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

}