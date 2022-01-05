package boblovespi.factoryautomation.common.block.fluid;

import boblovespi.factoryautomation.common.block.FABaseBlock;
import boblovespi.factoryautomation.common.tileentity.ITickable;
import boblovespi.factoryautomation.common.tileentity.mechanical.TEScrewPump;
import boblovespi.factoryautomation.common.util.FAItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ScrewPump extends FABaseBlock implements EntityBlock, SimpleWaterloggedBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

	public ScrewPump()
	{
		super("screw_pump", false, Properties.of(Material.METAL).requiresCorrectToolForDrops()
				.strength(2f), new Item.Properties().tab(FAItemGroups.mechanical));
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection())
				.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos())
						.getType() == Fluids.WATER);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, WATERLOGGED);
	}

	public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor world, BlockPos pos, BlockPos fromPos)
	{
		if (state.getValue(WATERLOGGED))
		{
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, dir, state1, world, pos, fromPos);
	}

	public FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TEScrewPump(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return ITickable::tickTE;
	}
}
