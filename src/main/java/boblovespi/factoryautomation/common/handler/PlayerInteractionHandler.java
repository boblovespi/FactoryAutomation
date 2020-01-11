package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.ModCompatHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
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
		Direction facing = event.getFace();
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

	@SubscribeEvent
	public static void OnPlayerSpawnEvent(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isRemote)
		{
			if (event.getEntity() instanceof PlayerEntity && !(event.getEntity() instanceof FakePlayer)) // is player
			{
				PlayerEntity player = (PlayerEntity) event.getEntity();
				if (!player.getEntityData().hasKey(PlayerEntity.PERSISTED_NBT_TAG))
					player.getEntityData().setTag(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());

				if (!player.getEntityData().getCompoundTag(PlayerEntity.PERSISTED_NBT_TAG).hasKey("faGuidebookReceived")
						&& Loader.isModLoaded("patchouli"))
				{
					player.addItemStackToInventory(ModCompatHandler.GetGuidebook());

					player.getEntityData().getCompoundTag(PlayerEntity.PERSISTED_NBT_TAG)
						  .setBoolean("faGuidebookReceived", true);
				}
			}
		}
	}
}
