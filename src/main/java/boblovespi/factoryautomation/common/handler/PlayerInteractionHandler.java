package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.util.ModCompatHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		World level = event.getWorld();

		if (item == Items.BUCKET && event.getEntityLiving().isCrouching())
		{
			event.setCanceled(true);
			if (!level.isClientSide)
			{
				if (facing != null)
				{
					stack.shrink(1);
					level.setBlockAndUpdate(pos.relative(facing), FABlocks.placedBucket.toBlock().defaultBlockState());
				}
			}
		}
	}

	@SubscribeEvent
	public static void OnPlayerSpawnEvent(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isClientSide)
		{
			if (event.getEntity() instanceof PlayerEntity && !(event.getEntity() instanceof FakePlayer)) // is player
			{
				PlayerEntity player = (PlayerEntity) event.getEntity();
				if (!player.getPersistentData().contains(PlayerEntity.PERSISTED_NBT_TAG))
					player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());

				if (!player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG)
						   .contains("faGuidebookReceived") && ModList.get().isLoaded("patchouli"))
				{
					player.addItem(ModCompatHandler.getGuidebook());

					player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG)
						  .putBoolean("faGuidebookReceived", true);
				}
			}
		}
	}
}
