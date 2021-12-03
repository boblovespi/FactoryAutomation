package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Created by Willi on 4/12/2017.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RiceGrain extends FABaseItem implements IPlantable
{
	public RiceGrain()
	{
		super("rice", CreativeModeTab.TAB_FOOD);
	}

	@Override
	public PlantType getPlantType(BlockGetter iBlockAccess, BlockPos blockPos)
	{
		return PlantType.WATER;
		// return EnumPlantType.Crop;
	}

	@Override
	public BlockState getPlant(BlockGetter iBlockAccess, BlockPos blockPos)
	{
		return FABlocks.riceCrop.ToBlock().defaultBlockState();
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		BlockState state = context.getLevel().getBlockState(context.getClickedPos().above());
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		Direction facing = context.getHorizontalDirection();
		ItemStack items = Objects.requireNonNull(player).getUseItem();
		Level world = context.getLevel();

		if (facing == Direction.UP && player.mayUseItemAt(pos.relative(facing), facing, items))
			if (state.getBlock().canSustainPlant(state, world, pos.above(), Direction.UP, this) && world
					.isEmptyBlock(pos.above(2)))
			{
				world.setBlockAndUpdate(pos.above(2), FABlocks.riceCrop.ToBlock().defaultBlockState());
				items.shrink(1);

				return InteractionResult.SUCCESS;
			}
		return InteractionResult.FAIL;
	}
}
