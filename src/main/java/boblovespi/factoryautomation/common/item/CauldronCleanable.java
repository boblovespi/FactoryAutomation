package boblovespi.factoryautomation.common.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		BlockState state = world.getBlockState(pos);
		ItemStack item = context.getItemInHand();

		if (!(state.getBlock() instanceof CauldronBlock))
			return InteractionResult.PASS;

		int i = state.getValue(CauldronBlock.LEVEL);

		if (i > 0)
		{
			if (!world.isClientSide)
			{
				item.shrink(1);

				if (player == null || !player.addItem(cleanedInto.copy()))
					world.addFreshEntity(
							new ItemEntity(world, pos.getX(), pos.getY() + 0.5f, pos.getZ(), cleanedInto.copy()));

				((CauldronBlock) Blocks.CAULDRON).setWaterLevel(world, pos, state, i - 1);
				if (player != null)
				player.awardStat(Stats.USE_CAULDRON);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
