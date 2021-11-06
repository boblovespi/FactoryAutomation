package boblovespi.factoryautomation.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Willi on 4/10/2018.
 */
public class CauldronCleanable extends FABaseItem
{
	private final ItemStack cleanedInto;

	public CauldronCleanable(String unlocalizedName, ItemGroup ct, ItemStack cleanedInto)
	{
		super(unlocalizedName, ct);
		this.cleanedInto = cleanedInto;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(pos);
		ItemStack item = context.getItemInHand();

		if (!(state.getBlock() instanceof CauldronBlock))
			return ActionResultType.PASS;

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

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}
}
