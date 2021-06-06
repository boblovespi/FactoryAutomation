package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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
		super("rice", ItemGroup.TAB_FOOD);
	}

	@Override
	public PlantType getPlantType(IBlockReader iBlockAccess, BlockPos blockPos)
	{
		return PlantType.WATER;
		// return EnumPlantType.Crop;
	}

	@Override
	public BlockState getPlant(IBlockReader iBlockAccess, BlockPos blockPos)
	{
		return FABlocks.riceCrop.toBlock().defaultBlockState();
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		BlockState state = context.getLevel().getBlockState(context.getClickedPos().above());
		BlockPos pos = context.getClickedPos();
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getHorizontalDirection();
		ItemStack items = Objects.requireNonNull(player).getUseItem();
		World world = context.getLevel();

		if (facing == Direction.UP && player.mayUseItemAt(pos.relative(facing), facing, items))
			if (state.getBlock().canSustainPlant(state, world, pos.above(), Direction.UP, this) && world
					.isEmptyBlock(pos.above(2)))
			{
				world.setBlockAndUpdate(pos.above(2), FABlocks.riceCrop.toBlock().defaultBlockState());
				items.shrink(1);

				return ActionResultType.SUCCESS;
			}
		return ActionResultType.FAIL;
	}
}
