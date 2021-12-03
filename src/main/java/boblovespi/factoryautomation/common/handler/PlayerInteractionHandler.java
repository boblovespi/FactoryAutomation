package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.ModCompatHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Willi on 6/27/2018.
 */
@Mod.EventBusSubscriber(modid = FactoryAutomation.MODID)
public class PlayerInteractionHandler
{
	@SubscribeEvent
	public static void OnRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		BlockPos pos = event.getPos();
		Direction facing = event.getFace();
		Level world = event.getWorld();

		if (item == Items.BUCKET && event.getEntityLiving().isCrouching())
		{
			event.setCanceled(true);
			if (!world.isClientSide)
			{
				if (facing != null)
				{
					stack.shrink(1);
					world.setBlockAndUpdate(pos.relative(facing), FABlocks.placedBucket.ToBlock().defaultBlockState());
				}
			}
		}
	}

	@SubscribeEvent
	public static void OnPlayerSpawnEvent(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isClientSide)
		{
			if (event.getEntity() instanceof Player && !(event.getEntity() instanceof FakePlayer)) // is player
			{
				Player player = (Player) event.getEntity();
				if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG))
					player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());

				if (!player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG)
						   .contains("faGuidebookReceived") && ModList.get().isLoaded("patchouli"))
				{
					player.addItem(ModCompatHandler.GetGuidebook());

					player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG)
						  .putBoolean("faGuidebookReceived", true);
				}
			}
		}
	}
}
