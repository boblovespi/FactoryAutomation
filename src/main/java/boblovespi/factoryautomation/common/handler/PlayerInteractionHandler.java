package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Willi on 6/27/2018.
 */
@Mod.EventBusSubscriber
public class PlayerInteractionHandler
{
	@SubscribeEvent
	public static void OnRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockPos pos = event.getPos();
		EnumFacing facing = event.getFace();
		World world = event.getWorld();

		if (item == Items.BUCKET && event.getEntityPlayer().isSneaking())
		{
			event.setCanceled(true);
			if (!world.isRemote)
			{
				if (facing != null)
				{
					stack.shrink(1);
					world.setBlockState(pos.offset(facing), FABlocks.placedBucket.ToBlock().getDefaultState());
				}
			}
		}
	}
}
