package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.TileEntityHandler;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEBevelGear;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Willi on 6/20/2019.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BevelGear extends FABaseBlock implements EntityBlock
{
	public static IntegerProperty LAYER = IntegerProperty.create("layer", 0, 2);
	public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public BevelGear()
	{
		super(Material.METAL, "bevel_gear", FAItemGroups.mechanical);
		TileEntityHandler.tiles.add(TEBevelGear.class);
	}

	public static Direction GetNegative(BlockState state)
	{
		if (state.getValue(LAYER) == 1)
			return state.getValue(FACING).getClockWise();
		else if (state.getValue(LAYER) == 0)
			return Direction.DOWN;
		else
			return Direction.UP;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LAYER, FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEBevelGear(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		BlockState state = defaultBlockState();
		if (context.getHorizontalDirection() == Direction.UP)
			state = state.setValue(LAYER, 0);
		else if (context.getHorizontalDirection() == Direction.DOWN)
			state = state.setValue(LAYER, 1);
		else
			state = state
					.setValue(LAYER, context.getClickLocation().y > 2 / 3f ? 2 : context.getClickLocation().y > 1 / 3f ? 1 : 0);
		if (context.getHorizontalDirection().get2DDataValue() >= 0)
			state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
		else
			state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
		return state;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}
}
