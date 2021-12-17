package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Willi on 4/10/2018.
 */
public class CauldronCleanable extends FABaseItem
{
	private final ItemStack cleanedInto;

	public CauldronCleanable(String unlocalizedName, CreativeModeTab ct, ItemStack cleanedInto)
	{
		super(unlocalizedName, ct);
		this.cleanedInto = cleanedInto;
		CauldronInteraction.WATER.put(this, new CleanInteraction());
	}

	public class CleanInteraction implements CauldronInteraction
	{
		@Override
		public InteractionResult interact(BlockState state, Level world, BlockPos pos, Player player,
										  InteractionHand hand, ItemStack stack)
		{
			if (!world.isClientSide)
			{
				stack.shrink(1);
				ItemHelper.PutItemsInInventoryOrDrop(player, cleanedInto, world);
				LayeredCauldronBlock.lowerFillLevel(state, world, pos);
				player.awardStat(Stats.USE_CAULDRON);
			}
			return InteractionResult.SUCCESS;
		}
	}
}
