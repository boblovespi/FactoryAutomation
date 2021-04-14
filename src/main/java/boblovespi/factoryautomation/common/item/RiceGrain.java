package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.common.block.FABlocks;
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

/**
 * Created by Willi on 4/12/2017.
 */
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
		return FABlocks.riceCrop.ToBlock().getDefaultState();
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getPos().up());
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getFace();
		ItemStack items = player.getActiveItemStack();
		World world = context.getWorld();

		if (facing == Direction.UP && player.canPlayerEdit(pos.offset(facing), facing, items))
			if (state.getBlock().canSustainPlant(state, world, pos.up(), Direction.UP, this) && world
					.isAirBlock(pos.up(2)))
			{
				world.setBlockState(pos.up(2), FABlocks.riceCrop.ToBlock().getDefaultState());
				items.shrink(1);

				return ActionResultType.SUCCESS;
			}
		return ActionResultType.FAIL;
	}
}
