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
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(pos);
		ItemStack item = context.getItem();

		if (!(state.getBlock() instanceof CauldronBlock))
			return ActionResultType.PASS;

		int i = state.get(CauldronBlock.LEVEL);

		if (i > 0)
		{
			if (!world.isRemote)
			{
				item.shrink(1);

				if (player == null || !player.addItemStackToInventory(cleanedInto.copy()))
					world.addEntity(
							new ItemEntity(world, pos.getX(), pos.getY() + 0.5f, pos.getZ(), cleanedInto.copy()));

				((CauldronBlock) Blocks.CAULDRON).setWaterLevel(world, pos, state, i - 1);
				if (player != null)
				player.addStat(Stats.USE_CAULDRON);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}
}
